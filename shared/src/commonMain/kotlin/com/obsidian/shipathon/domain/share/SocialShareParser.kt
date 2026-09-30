package com.obsidian.shipathon.domain.share

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

/**
 * Intelligent parser that extracts clean game titles or search queries from:
 * 1. TikTok video captions and share URLs
 * 2. Instagram Reel / Post captions and links
 * 3. YouTube & YouTube Shorts links and video titles
 * 4. Steam store links
 * 5. General social media captions with hashtags, mentions, and filler words
 */
object SocialShareParser {

    private val HASHTAG_REGEX = Regex("#[A-Za-z0-9_]+")
    private val MENTION_REGEX = Regex("@[A-Za-z0-9_.]+")
    private val URL_REGEX = Regex("https?://\\S+")
    private val STEAM_URL_REGEX = Regex("store\\.steampowered\\.com/app/\\d+/([^/?#]+)", RegexOption.IGNORE_CASE)
    private val TIKTOK_URL_REGEX = Regex("tiktok\\.com", RegexOption.IGNORE_CASE)
    private val INSTA_URL_REGEX = Regex("instagram\\.com", RegexOption.IGNORE_CASE)
    private val YOUTUBE_URL_REGEX = Regex("(?:youtube\\.com|youtu\\.be)", RegexOption.IGNORE_CASE)
    private val TWITCH_URL_REGEX = Regex("twitch\\.tv", RegexOption.IGNORE_CASE)
    private val REDDIT_URL_REGEX = Regex("reddit\\.com", RegexOption.IGNORE_CASE)
    private val TWITTER_URL_REGEX = Regex("(?:twitter\\.com|x\\.com)", RegexOption.IGNORE_CASE)

    // Common filler words / prefixes in social shares (sorted longest first to avoid partial truncation)
    private val NOISE_PREFIXES = listOf(
        "check out this gameplay of",
        "check out this video of",
        "check out this game",
        "check out this",
        "check out",
        "gameplay of",
        "gameplay",
        "video of",
        "trailer of",
        "trailer for",
        "walkthrough of",
        "walkthrough for",
        "review of",
        "stream of",
        "streaming",
        "finally playing",
        "can't believe this game",
        "you need to play",
        "must play game",
        "must play",
        "watch this",
        "look at this",
        "this game is",
        "game name is",
        "game name:",
        "game name",
        "game title:",
        "game title",
        "game:",
        "title:",
        "playing",
        "new game",
        "play of",
    )

    // Common trailing words / suffixes in social shares
    private val NOISE_SUFFIXES = listOf(
        "early access",
        "closed beta",
        "open beta",
        "no commentary",
        "full gameplay",
        "full playthrough",
        "full walkthrough",
        "full trailer",
        "gameplay",
        "walkthrough",
        "playthrough",
        "trailer",
        "teaser",
        "part 1",
        "part 2",
        "part 3",
        "pt 1",
        "pt 2",
        "pt 3",
        "episode 1",
        "ep 1",
        "ep 2",
        "full game",
        "today",
        "now",
        "live",
        "stream",
        "review",
        "reaction",
        "clips",
        "shorts",
        "reel",
        "video",
        "beta",
        "demo",
    )

    /**
     * Parses raw incoming shared text (which may contain URLs, captions, hashtags)
     * and returns the best candidate game query to search on IGDB.
     */
    fun parse(rawText: String): ShareParseResult {
        val trimmed = rawText.trim()
        if (trimmed.isBlank()) {
            return ShareParseResult(
                originalText = rawText,
                extractedQuery = "",
                sourceType = ShareSourceType.MANUAL_INPUT,
            )
        }

        // 1. Check for Steam Store link (e.g. store.steampowered.com/app/1091500/Cyberpunk_2077)
        val steamMatch = STEAM_URL_REGEX.find(trimmed)
        if (steamMatch != null) {
            val rawName = steamMatch.groupValues[1]
            val cleaned = rawName.replace('_', ' ').replace('-', ' ').trim()
            return ShareParseResult(
                originalText = rawText,
                extractedQuery = cleaned,
                sourceType = ShareSourceType.STEAM,
            )
        }

        // 2. Identify platform source
        val sourceType = when {
            TIKTOK_URL_REGEX.containsMatchIn(trimmed) -> ShareSourceType.TIKTOK
            INSTA_URL_REGEX.containsMatchIn(trimmed) -> ShareSourceType.INSTAGRAM
            YOUTUBE_URL_REGEX.containsMatchIn(trimmed) -> ShareSourceType.YOUTUBE
            TWITCH_URL_REGEX.containsMatchIn(trimmed) -> ShareSourceType.TWITCH
            REDDIT_URL_REGEX.containsMatchIn(trimmed) -> ShareSourceType.REDDIT
            TWITTER_URL_REGEX.containsMatchIn(trimmed) -> ShareSourceType.TWITTER
            URL_REGEX.containsMatchIn(trimmed) -> ShareSourceType.WEB_LINK
            else -> ShareSourceType.TEXT_CLIPBOARD
        }

        // 3. Extract text content without URLs
        var cleanedText = trimmed
            .replace(URL_REGEX, " ")
            .replace(HASHTAG_REGEX, " ")
            .replace(MENTION_REGEX, " ")
            .replace(Regex("[\r\n\t]+"), " ")
            .replace(Regex("[🎮🔥✨👀💥🎯🕹️👾]"), " ")
            .trim()

        // 4. Strip common introductory noise phrases and trailing suffixes iteratively (longest first)
        val sortedPrefixes = NOISE_PREFIXES.sortedByDescending { it.length }
        val sortedSuffixes = NOISE_SUFFIXES.sortedByDescending { it.length }
        var changed = true
        while (changed) {
            changed = false
            for (noise in sortedPrefixes) {
                if (cleanedText.startsWith(noise, ignoreCase = true)) {
                    cleanedText = cleanedText.substring(noise.length).trim()
                    changed = true
                }
            }
            for (noise in sortedSuffixes) {
                if (cleanedText.endsWith(noise, ignoreCase = true)) {
                    cleanedText = cleanedText.substring(0, cleanedText.length - noise.length).trim()
                    changed = true
                }
            }
            cleanedText = cleanedText.trim { it <= ' ' || it in ":-–—|\"'/!?,." }
        }

        // 5. If caption was long, take the first line or sentence
        val firstSentence = cleanedText.split(Regex("[.!?|\\-]")).firstOrNull()?.trim().orEmpty()
        var candidate = if (firstSentence.length in 2..60) firstSentence else cleanedText.take(50).trim()

        // 6. If candidate is blank (bare URL shared), extract slug from URL path (e.g. playables, games, shorts)
        if (candidate.isBlank()) {
            val urlMatch = URL_REGEX.find(trimmed)?.value
            if (urlMatch != null) {
                val pathSegments = urlMatch
                    .substringBefore('?')
                    .substringBefore('#')
                    .removeSuffix("/")
                    .split('/')
                    .filter { it.isNotBlank() && !it.contains("http") && !it.contains("www.") && !it.contains(".com") && !it.contains(".be") && !it.contains(".tv") }

                val lastSegment = pathSegments.lastOrNull()?.takeIf { it.length > 2 && !it.all { ch -> ch.isDigit() } }
                if (lastSegment != null) {
                    val decoded = lastSegment.replace('_', ' ').replace('-', ' ').replace("%20", " ").trim()
                    if (decoded.length in 2..50 && !decoded.equals("watch", ignoreCase = true) && !decoded.equals("shorts", ignoreCase = true) && !decoded.equals("playables", ignoreCase = true)) {
                        candidate = decoded
                    }
                }
            }
        }

        return ShareParseResult(
            originalText = rawText,
            extractedQuery = candidate,
            sourceType = sourceType,
        )
    }

    /**
     * Asynchronously resolves titles from any bare link on the web:
     * 1. YouTube & TikTok via oEmbed API
     * 2. Reddit via JSON API
     * 3. Any web URL via OpenGraph (og:title) and HTML <title> scraping
     */
    suspend fun resolveVideoTitle(rawUrl: String): String? {
        return try {
            val trimmed = rawUrl.trim()
            val urlMatch = URL_REGEX.find(trimmed)?.value ?: return null
            val client = HttpClient()

            val title: String? = when {
                YOUTUBE_URL_REGEX.containsMatchIn(urlMatch) -> {
                    val oembedUrl = "https://www.youtube.com/oembed?url=${urlMatch.substringBefore('?')}&format=json"
                    val response = client.get(oembedUrl).bodyAsText()
                    Regex("\"title\"\\s*:\\s*\"([^\"]+)\"").find(response)?.groupValues?.getOrNull(1)
                }
                TIKTOK_URL_REGEX.containsMatchIn(urlMatch) -> {
                    val oembedUrl = "https://www.tiktok.com/oembed?url=${urlMatch}"
                    val response = client.get(oembedUrl).bodyAsText()
                    Regex("\"title\"\\s*:\\s*\"([^\"]+)\"").find(response)?.groupValues?.getOrNull(1)
                }
                REDDIT_URL_REGEX.containsMatchIn(urlMatch) -> {
                    val jsonUrl = "${urlMatch.substringBefore('?').removeSuffix("/")}.json"
                    val response = client.get(jsonUrl).bodyAsText()
                    Regex("\"title\"\\s*:\\s*\"([^\"]+)\"").find(response)?.groupValues?.getOrNull(1)
                }
                else -> {
                    // Universal Web Fallback: Fetch page and extract og:title or <title>
                    val html = client.get(urlMatch).bodyAsText()
                    val ogMatch = Regex("<meta\\s+(?:property|name)=[\"']og:title[\"']\\s+content=[\"']([^\"']+)[\"']", RegexOption.IGNORE_CASE).find(html)
                        ?: Regex("<meta\\s+content=[\"']([^\"']+)[\"']\\s+(?:property|name)=[\"']og:title[\"']", RegexOption.IGNORE_CASE).find(html)
                    
                    ogMatch?.groupValues?.getOrNull(1)
                        ?: Regex("<title(?:\\s+[^>]*)?>([^<]+)</title>", RegexOption.IGNORE_CASE).find(html)?.groupValues?.getOrNull(1)
                }
            }
            client.close()

            title?.replace("&amp;", "&")
                ?.replace("&#39;", "'")
                ?.replace("&quot;", "\"")
                ?.replace("&lt;", "<")
                ?.replace("&gt;", ">")
                ?.replace("\\u0026", "&")
                ?.replace("\\u2014", "—")
                ?.replace("\\u2013", "-")
                ?.replace("\\u0027", "'")
                ?.replace("\\\"", "\"")
                ?.trim()
        } catch (_: Exception) {
            null
        }
    }
}

enum class ShareSourceType(val displayName: String, val categoryName: String, val icon: String) {
    TIKTOK("TikTok", "Discovery", "🎵"),
    INSTAGRAM("Instagram", "Discovery", "📷"),
    YOUTUBE("YouTube", "Discovery", "▶️"),
    TWITCH("Twitch", "Discovery", "🟣"),
    REDDIT("Reddit", "Discovery", "🟠"),
    TWITTER("X / Twitter", "Discovery", "🐦"),
    STEAM("Steam", "Import", "🎮"),
    WEB_LINK("Web Link", "Discovery", "🔗"),
    TEXT_CLIPBOARD("Smart Clip", "Discovery", "📝"),
    MANUAL_INPUT("Social Video", "Discovery", "✨"),
}

data class ShareParseResult(
    val originalText: String,
    val extractedQuery: String,
    val sourceType: ShareSourceType,
)
