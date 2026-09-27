package com.obsidian.shipathon.domain.library

enum class LibraryStatus {
    BACKLOG,
    PLAYING,
    COMPLETED,
}

fun LibraryStatus.displayName(): String = when (this) {
    LibraryStatus.BACKLOG -> "Backlog"
    LibraryStatus.PLAYING -> "Playing"
    LibraryStatus.COMPLETED -> "Completed"
}

fun String.toLibraryStatusOrNull(): LibraryStatus? =
    LibraryStatus.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }


