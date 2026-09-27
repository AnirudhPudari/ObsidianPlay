package com.obsidian.shipathon

import com.obsidian.shipathon.domain.share.ShareSourceType
import com.obsidian.shipathon.domain.share.SocialShareParser
import kotlin.test.Test
import kotlin.test.assertEquals

class SocialShareParserTest {

    @Test
    fun testSteamUrlExtraction() {
        val input = "https://store.steampowered.com/app/1091500/Cyberpunk_2077/"
        val result = SocialShareParser.parse(input)
        assertEquals(ShareSourceType.STEAM, result.sourceType)
        assertEquals("Cyberpunk 2077", result.extractedQuery)
    }

    @Test
    fun testTikTokCaptionWithHashtags() {
        val input = "OMG this new horror game Silent Hill 2 is insane! #gaming #fyp #silenthill https://vt.tiktok.com/ZSjX9a/"
        val result = SocialShareParser.parse(input)
        assertEquals(ShareSourceType.TIKTOK, result.sourceType)
        assertEquals("OMG this new horror game Silent Hill 2 is insane", result.extractedQuery)
    }

    @Test
    fun testNoisePrefixRemoval() {
        val input = "Gameplay of Hades II is so good! #hades #indiegame"
        val result = SocialShareParser.parse(input)
        assertEquals("Hades II is so good", result.extractedQuery)
    }

    @Test
    fun testInstagramReelShare() {
        val input = "You need to play Clair Obscur Expedition 33! Check this out https://www.instagram.com/reel/DC_xyz/ #gaming"
        val result = SocialShareParser.parse(input)
        assertEquals(ShareSourceType.INSTAGRAM, result.sourceType)
        assertEquals("Clair Obscur Expedition 33", result.extractedQuery)
    }
}
