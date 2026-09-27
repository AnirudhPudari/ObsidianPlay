package com.obsidian.shipathon.data.local.db

import com.obsidian.shipathon.domain.library.LibraryRepository

actual fun createLibraryRepository(): LibraryRepository = InMemoryLibraryRepository()

