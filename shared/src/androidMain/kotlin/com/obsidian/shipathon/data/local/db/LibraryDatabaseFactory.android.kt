package com.obsidian.shipathon.data.local.db

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.obsidian.shipathon.domain.library.LibraryRepository

actual fun createLibraryRepository(): LibraryRepository {
    val driver = AndroidSqliteDriver(
        schema = ObsidianDatabase.Schema,
        context = AndroidAppContextHolder.context,
        name = "obsidianplay.db",
    )
    return SqlDelightLibraryRepository(ObsidianDatabase(driver))
}

