package com.obsidian.shipathon.di

import com.obsidian.shipathon.data.local.db.createLibraryRepository
import com.obsidian.shipathon.data.remote.igdb.IgdbGameRepository
import com.obsidian.shipathon.domain.GameRepository
import com.obsidian.shipathon.domain.library.LibraryRepository
import com.obsidian.shipathon.domain.subscription.InMemoryProSubscriptionRepository
import com.obsidian.shipathon.domain.subscription.ProSubscriptionRepository

/** Single manual composition point. Swap gameRepository's implementation to change data source. */
object AppContainer {
    val gameRepository: GameRepository by lazy { IgdbGameRepository() }
    val libraryRepository: LibraryRepository by lazy { createLibraryRepository() }
    var subscriptionRepository: ProSubscriptionRepository = InMemoryProSubscriptionRepository()
}
