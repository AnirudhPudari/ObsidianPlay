package com.obsidian.shipathon.data.local.db

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.obsidian.shipathon.domain.library.LibraryRepository

actual fun createLibraryRepository(): LibraryRepository {
    val driver = NativeSqliteDriver(ObsidianDatabase.Schema, "obsidianplay.db")
    return SqlDelightLibraryRepository(ObsidianDatabase(driver))
}

