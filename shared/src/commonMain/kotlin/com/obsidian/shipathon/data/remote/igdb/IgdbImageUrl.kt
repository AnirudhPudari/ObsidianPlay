package com.obsidian.shipathon.data.remote.igdb

internal enum class IgdbImageSize(val urlSegment: String) {
    CoverSmall("cover_small"),
    CoverBig("cover_big"),
    ScreenshotMed("screenshot_med"),
    ScreenshotBig("screenshot_big"),
}

internal fun igdbImageUrl(imageId: String?, size: IgdbImageSize): String? {
    if (imageId.isNullOrBlank()) return null
    return "https://images.igdb.com/igdb/image/upload/t_${size.urlSegment}/$imageId.jpg"
}
