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

    private val BRACKET_REGEX = Regex("\\[[^\\]]*\\]|\\([^\\)]*\\)|\\{[^\\}]*\\}|【[^】]*】")

    // Common trailing words / suffixes in social shares
    private val NOISE_SUFFIXES = listOf(
        "official gameplay trailer",
        "official reveal trailer",
        "official launch trailer",
        "official announcement trailer",
        "official teaser trailer",
        "official gameplay",
        "official trailer",
        "announcement trailer",
        "gameplay trailer",
        "reveal trailer",
        "launch trailer",
        "teaser trailer",
        "gameplay walkthrough full game",
        "full game walkthrough",
        "gameplay walkthrough",
        "walkthrough full game",
        "full game playthrough",
        "gameplay playthrough",
        "full playthrough",
        "full walkthrough",
        "full gameplay",
        "full stream",
        "full game",
        "gameplay",
        "walkthrough",
        "playthrough",
        "longplay",
        "speedrun",
        "no commentary",
        "is a masterpiece",
        "is a masterpiece...",
        "is a master piece",
        "is unbelievable",
        "is incredible",
        "is amazing",
        "is insane",
        "is peak",
        "is so good",
        "is the best",
        "my thoughts and review",
        "my thoughts on",
        "my thoughts",
        "honest review",
        "full review",
        "game review",
        "video essay",
        "retrospective",
        "critique",
        "analysis",
        "impressions",
        "first impressions",
        "early access",
        "closed beta",
        "open beta",
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
        "4k 60fps ps5",
        "4k 60fps hdr",
        "4k 60fps",
        "60fps",
        "4k hdr",
        "4k",
        "1080p",
        "ray tracing",
        "ps5",
        "ps4",
        "xbox series x",
        "xbox",
        "pc ultra",
        "pc",
        "nintendo switch",
    )

    private fun stripNoise(text: String): String {
        var result = text.replace(BRACKET_REGEX, " ").trim()
        val sortedPrefixes = NOISE_PREFIXES.sortedByDescending { it.length }
        val sortedSuffixes = NOISE_SUFFIXES.sortedByDescending { it.length }
        var changed = true
        while (changed) {
            changed = false
            for (noise in sortedPrefixes) {
                if (result.startsWith(noise, ignoreCase = true)) {
                    result = result.substring(noise.length).trim()
                    changed = true
                }
            }
            for (noise in sortedSuffixes) {
                if (result.endsWith(noise, ignoreCase = true)) {
                    result = result.substring(0, result.length - noise.length).trim()
                    changed = true
                }
            }
            result = result.trim { it <= ' ' || it in ":-–—|\"'/!?,." }
        }
        return result.replace(Regex("\\s+"), " ").trim()
    }

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

        // 4. Strip noise from full text
        cleanedText = stripNoise(cleanedText)

        // 5. Split by sentence or title separators (—, –, -, |, :, etc.) and pick the cleanest segment
        val segments = cleanedText.split(Regex("[.!?|\\-—–:]")).map { stripNoise(it) }.filter { it.isNotBlank() }
        val bestSegment = segments.firstOrNull { it.length in 2..60 } ?: cleanedText.take(50).trim()
        var candidate = stripNoise(bestSegment)

        // 6. If candidate is blank (bare URL shared), only extract meaningful slug if it's not an opaque video ID / hash
        if (candidate.isBlank()) {
            val urlMatch = URL_REGEX.find(trimmed)?.value
            if (urlMatch != null && !YOUTUBE_URL_REGEX.containsMatchIn(urlMatch) && !TIKTOK_URL_REGEX.containsMatchIn(urlMatch) && !INSTA_URL_REGEX.containsMatchIn(urlMatch)) {
                val pathSegments = urlMatch
                    .substringBefore('?')
                    .substringBefore('#')
                    .removeSuffix("/")
                    .split('/')
                    .filter { it.isNotBlank() && !it.contains("http") && !it.contains("www.") && !it.contains(".com") && !it.contains(".be") && !it.contains(".tv") && !it.contains(".org") && !it.contains(".net") }

                val lastSegment = pathSegments.lastOrNull()?.takeIf { it.length > 2 && !it.all { ch -> ch.isDigit() } }
                if (lastSegment != null) {
                    val decoded = lastSegment.replace('_', ' ').replace('-', ' ').replace("%20", " ").trim()
                    if (decoded.length in 3..50 && !decoded.equals("watch", ignoreCase = true) && !decoded.equals("shorts", ignoreCase = true) && !decoded.equals("playables", ignoreCase = true) && !decoded.matches(Regex("^[a-zA-Z0-9_-]{10,12}$"))) {
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

    private val httpClient by lazy {
        io.ktor.client.HttpClient {
            install(io.ktor.client.plugins.HttpTimeout) {
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 20_000
                requestTimeoutMillis = 25_000
            }
        }
    }

    /**
     * Extracts YouTube Video ID from any YouTube URL format (youtu.be, watch?v=, shorts/, live/, playables)
     */
    fun extractYouTubeVideoId(url: String): String? {
        val patterns = listOf(
            Regex("(?:youtu\\.be/|youtube\\.com/(?:embed/|v/|shorts/|live/|watch\\?v=|watch\\?.+&v=))([a-zA-Z0-9_-]{11})"),
            Regex("youtube\\.com/watch\\?v=([a-zA-Z0-9_-]{11})"),
        )
        for (pattern in patterns) {
            val match = pattern.find(url)
            if (match != null) return match.groupValues[1]
        }
        return null
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
            val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"

            val title: String? = when {
                YOUTUBE_URL_REGEX.containsMatchIn(urlMatch) -> {
                    val videoId = extractYouTubeVideoId(urlMatch)
                    val targetUrl = if (videoId != null) "https://www.youtube.com/watch?v=$videoId" else urlMatch.substringBefore('?')
                    val oembedUrl = "https://www.youtube.com/oembed?url=${targetUrl}&format=json"
                    val response = httpClient.get(oembedUrl) {
                        headers.append("User-Agent", userAgent)
                    }.bodyAsText()
                    Regex("\"title\"\\s*:\\s*\"([^\"]+)\"").find(response)?.groupValues?.getOrNull(1)
                }
                TIKTOK_URL_REGEX.containsMatchIn(urlMatch) -> {
                    val oembedUrl = "https://www.tiktok.com/oembed?url=${urlMatch}"
                    val response = httpClient.get(oembedUrl) {
                        headers.append("User-Agent", userAgent)
                    }.bodyAsText()
                    Regex("\"title\"\\s*:\\s*\"([^\"]+)\"").find(response)?.groupValues?.getOrNull(1)
                }
                REDDIT_URL_REGEX.containsMatchIn(urlMatch) -> {
                    val jsonUrl = "${urlMatch.substringBefore('?').removeSuffix("/")}.json"
                    val response = httpClient.get(jsonUrl) {
                        headers.append("User-Agent", userAgent)
                    }.bodyAsText()
                    Regex("\"title\"\\s*:\\s*\"([^\"]+)\"").find(response)?.groupValues?.getOrNull(1)
                }
                else -> {
                    // Universal Web Fallback: Fetch page and extract og:title or <title>
                    val html = httpClient.get(urlMatch) {
                        headers.append("User-Agent", userAgent)
                    }.bodyAsText()
                    val ogMatch = Regex("<meta\\s+(?:property|name)=[\"']og:title[\"']\\s+content=[\"']([^\"']+)[\"']", RegexOption.IGNORE_CASE).find(html)
                        ?: Regex("<meta\\s+content=[\"']([^\"']+)[\"']\\s+(?:property|name)=[\"']og:title[\"']", RegexOption.IGNORE_CASE).find(html)
                    
                    ogMatch?.groupValues?.getOrNull(1)
                        ?: Regex("<title(?:\\s+[^>]*)?>([^<]+)</title>", RegexOption.IGNORE_CASE).find(html)?.groupValues?.getOrNull(1)
                }
            }

            val cleanedTitle = title?.replace("&amp;", "&")
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

            val genericBrandTitles = setOf(
                "instagram",
                "login • instagram",
                "login on instagram",
                "instagram photo",
                "instagram video",
                "tiktok - make your day",
                "tiktok",
                "youtube",
                "reddit",
                "reddit - dive into anything",
                "twitter",
                "x",
            )

            if (cleanedTitle != null && (genericBrandTitles.contains(cleanedTitle.lowercase().trim()) || cleanedTitle.startsWith("login • instagram", ignoreCase = true))) {
                null
            } else {
                cleanedTitle
            }
        } catch (e: Exception) {
            println("Error resolving video title: ${e.message}")
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
