package com.obsidian.shipathon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidian.shipathon.di.AppContainer
import com.obsidian.shipathon.domain.library.LibraryStatus
import com.obsidian.shipathon.domain.model.Game
import com.obsidian.shipathon.domain.share.ShareIntentHolder
import com.obsidian.shipathon.domain.subscription.SubscriptionPlan
import com.obsidian.shipathon.ui.celebration.CompletionCelebrationDialog
import com.obsidian.shipathon.ui.detail.GameDetailSheet
import com.obsidian.shipathon.ui.dialog.RemoveGameConfirmationDialog
import com.obsidian.shipathon.ui.home.HomeScreen
import com.obsidian.shipathon.ui.library.LibraryScreen
import com.obsidian.shipathon.ui.paywall.ObsidianProPaywallDialog
import com.obsidian.shipathon.ui.profile.ProfileScreen
import com.obsidian.shipathon.ui.roulette.BacklogRouletteDialog
import com.obsidian.shipathon.ui.search.SearchScreen
import com.obsidian.shipathon.ui.share.QuickShareImportDialog
import com.obsidian.shipathon.ui.share.ShareCardDialog
import com.obsidian.shipathon.ui.splash.SplashScreen
import com.obsidian.shipathon.ui.theme.DialogBorderGlowBrush
import com.obsidian.shipathon.ui.theme.GamingBackground
import com.obsidian.shipathon.ui.theme.GamingBlue
import com.obsidian.shipathon.ui.theme.GamingBorder
import com.obsidian.shipathon.ui.theme.GamingCard
import com.obsidian.shipathon.ui.theme.GamingCardElevated
import com.obsidian.shipathon.ui.theme.GamingCyan
import com.obsidian.shipathon.ui.theme.GamingEmerald
import com.obsidian.shipathon.ui.theme.GamingGold
import com.obsidian.shipathon.ui.theme.GamingStat
import com.obsidian.shipathon.ui.theme.GamingTextPrimary
import com.obsidian.shipathon.ui.theme.GamingTextSecondary
import com.obsidian.shipathon.ui.theme.GlassBorderBrush
import com.obsidian.shipathon.ui.theme.GoldGradient
import com.obsidian.shipathon.ui.theme.HeaderGlowBrush
import com.obsidian.shipathon.ui.theme.ObsidianIcons
import com.obsidian.shipathon.ui.theme.ObsidianPlayTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class AppTab(val title: String) {
    Home("ObsidianPlay"),
    Library("Bucket List"),
    Search("Discover Games"),
    Profile("Gamer Profile"),
}

@Composable
fun App() {
    ObsidianPlayTheme {
        var isSplashVisible by remember { mutableStateOf(true) }
        val tabBackStack = remember { androidx.compose.runtime.mutableStateListOf(AppTab.Home) }
        val selectedTab = tabBackStack.lastOrNull() ?: AppTab.Home

        fun selectTab(newTab: AppTab) {
            if (tabBackStack.lastOrNull() != newTab) {
                tabBackStack.add(newTab)
                if (tabBackStack.size > 25) {
                    tabBackStack.removeAt(0)
                }
            }
        }

        var popularGames by remember { mutableStateOf<List<Game>>(emptyList()) }
        var newReleases  by remember { mutableStateOf<List<Game>>(emptyList()) }
        val libraryGames by AppContainer.libraryRepository.entries.collectAsState()
        val subscriptionState by AppContainer.subscriptionRepository.subscriptionState.collectAsState()
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var isLoading    by remember { mutableStateOf(true) }

        // Modal / Sheet Dialog States
        var selectedGameForDetails by remember { mutableStateOf<Game?>(null) }
        var gameForShareCard by remember { mutableStateOf<Game?>(null) }
        var celebrationData by remember { mutableStateOf<Pair<Game, Double>?>(null) }
        var showRouletteDialog by remember { mutableStateOf(false) }
        var showProPaywallDialog by remember { mutableStateOf(false) }
        var showWrappedDialog by remember { mutableStateOf(false) }
        var manualSocialPasteActive by remember { mutableStateOf(false) }
        var gameToRemove by remember { mutableStateOf<Game?>(null) }
        var toastMessage by remember { mutableStateOf<String?>(null) }

        // Listen for incoming system Share Sheet text
        val pendingShareText by ShareIntentHolder.pendingShareText.collectAsState()

        val coroutineScope = rememberCoroutineScope()

        // ── Predictive / System Back Navigation Handler ───────────────────────
        val hasActiveModal = selectedGameForDetails != null ||
                gameForShareCard != null ||
                celebrationData != null ||
                showRouletteDialog ||
                showProPaywallDialog ||
                showWrappedDialog ||
                manualSocialPasteActive ||
                gameToRemove != null

        val canGoBackInTabs = tabBackStack.size > 1 || (tabBackStack.isNotEmpty() && tabBackStack.last() != AppTab.Home)
        val backHandlerEnabled = !isSplashVisible && (hasActiveModal || canGoBackInTabs)

        BackHandler(enabled = backHandlerEnabled) {
            when {
                selectedGameForDetails != null -> selectedGameForDetails = null
                gameForShareCard != null -> gameForShareCard = null
                celebrationData != null -> celebrationData = null
                showRouletteDialog -> showRouletteDialog = false
                showProPaywallDialog -> showProPaywallDialog = false
                showWrappedDialog -> showWrappedDialog = false
                manualSocialPasteActive -> manualSocialPasteActive = false
                gameToRemove != null -> gameToRemove = null
                tabBackStack.size > 1 -> tabBackStack.removeAt(tabBackStack.lastIndex)
                tabBackStack.isNotEmpty() && tabBackStack.last() != AppTab.Home -> {
                    tabBackStack.clear()
                    tabBackStack.add(AppTab.Home)
                }
            }
        }

        fun showToast(msg: String) {
            coroutineScope.launch {
                toastMessage = msg
                delay(2500)
                if (toastMessage == msg) {
                    toastMessage = null
                }
            }
        }

        fun fetchGames() {
            coroutineScope.launch {
                isLoading = true
                errorMessage = null
                val popularJob     = async { AppContainer.gameRepository.getPopularGames() }
                val newReleasesJob = async { AppContainer.gameRepository.getNewReleases() }

                popularJob.await()
                    .onSuccess { popularGames = it }
                    .onFailure { errorMessage = it.message ?: "Unknown error" }
                newReleasesJob.await()
                    .onSuccess { newReleases = it }

                isLoading = false
            }
        }

        LaunchedEffect(Unit) {
            val minSplashTimer = async { delay(1300) }
            val popularJob     = async { AppContainer.gameRepository.getPopularGames() }
            val newReleasesJob = async { AppContainer.gameRepository.getNewReleases() }

            popularJob.await()
                .onSuccess { popularGames = it }
                .onFailure { errorMessage = it.message ?: "Unknown error" }
            newReleasesJob.await()
                .onSuccess { newReleases = it }

            minSplashTimer.await()
            isLoading = false
            isSplashVisible = false
        }

        Crossfade(
            targetState = isSplashVisible,
            animationSpec = tween(600),
        ) { splashActive ->
            if (splashActive) {
                SplashScreen()
            } else {
                Scaffold(
                    containerColor = GamingBackground,
                    topBar = {
                        ObsidianTopBar(title = selectedTab.title, showLogo = selectedTab == AppTab.Home)
                    },
                    bottomBar = {
                        ObsidianNavBar(
                            selectedTab = selectedTab,
                            onTabSelected = { selectTab(it) },
                        )
                    },
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .background(GamingBackground),
                    ) {
                        when (selectedTab) {
                            AppTab.Home -> HomeScreen(
                                popularGames = popularGames,
                                newReleases  = newReleases,
                                libraryGames = libraryGames,
                                errorMessage = errorMessage,
                                isLoading    = isLoading,
                                onRetry      = { fetchGames() },
                                onToggleBacklog = { game ->
                                    val existing = libraryGames.firstOrNull { it.gameId == game.id }
                                    if (existing != null) {
                                        gameToRemove = game
                                    } else {
                                        coroutineScope.launch {
                                            AppContainer.libraryRepository.save(game, LibraryStatus.BACKLOG)
                                            showToast("✓ Added \"${game.name}\" to Backlog")
                                        }
                                    }
                                },
                                onGameClick = { game -> selectedGameForDetails = game },
                                onNavigateToSearch = { selectTab(AppTab.Search) },
                            )

                            AppTab.Library -> LibraryScreen(
                                libraryGames = libraryGames,
                                onGameClick = { game -> selectedGameForDetails = game },
                                onStatusChange = { game, newStatus ->
                                    coroutineScope.launch {
                                        AppContainer.libraryRepository.save(game, newStatus)
                                        if (newStatus == LibraryStatus.COMPLETED) {
                                            val entry = libraryGames.firstOrNull { it.gameId == game.id }
                                            celebrationData = Pair(game, entry?.playtimeHours ?: 0.0)
                                        } else if (newStatus == LibraryStatus.PLAYING) {
                                            showToast("▶ Now Playing \"${game.name}\"!")
                                        }
                                    }
                                },
                                onOpenRoulette = { showRouletteDialog = true },
                                onOpenShareCard = { game -> gameForShareCard = game },
                                onNavigateToSearch = { selectTab(AppTab.Search) },
                            )

                            AppTab.Search -> SearchScreen(
                                libraryGames = libraryGames,
                                onToggleBacklog = { game ->
                                    val existing = libraryGames.firstOrNull { it.gameId == game.id }
                                    if (existing != null) {
                                        gameToRemove = game
                                    } else {
                                        coroutineScope.launch {
                                            AppContainer.libraryRepository.save(game, LibraryStatus.BACKLOG)
                                            showToast("✓ Added \"${game.name}\" to Backlog")
                                        }
                                    }
                                },
                                onGameClick = { game -> selectedGameForDetails = game },
                                onOpenSocialPasteDialog = { manualSocialPasteActive = true },
                            )

                            AppTab.Profile -> ProfileScreen(
                                libraryGames = libraryGames,
                                isPro = subscriptionState.isPro,
                                onOpenPaywall = {
                                    val handled = AppContainer.subscriptionRepository.showPaywall()
                                    if (!handled) {
                                        showProPaywallDialog = true
                                    }
                                },
                                onOpenWrapped = { showWrappedDialog = true },
                                onToggleJudgePro = { enabled ->
                                    coroutineScope.launch {
                                        AppContainer.subscriptionRepository.setJudgeDemoPro(enabled)
                                        showToast(if (enabled) "🎉 Promo code accepted: PRO Unlocked!" else "Judges Mode: Pro Disabled")
                                    }
                                },
                            )
                        }

                        // ── Floating Glassmorphic Toast Notification ──────────────────
                        AnimatedVisibility(
                            visible = toastMessage != null,
                            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 16.dp, start = 20.dp, end = 20.dp),
                        ) {
                            toastMessage?.let { msg ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF131A2B).copy(alpha = 0.95f))
                                        .border(1.dp, Color(0x28FFFFFF), RoundedCornerShape(16.dp))
                                        .padding(horizontal = 18.dp, vertical = 12.dp),
                                ) {
                                    Text(
                                        text = msg,
                                        color = GamingTextPrimary,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Global Modals & Sheets ───────────────────────────────────────────

        // 1. Game Detail Bottom Sheet
        selectedGameForDetails?.let { game ->
            val libraryEntry = libraryGames.firstOrNull { it.gameId == game.id }
            GameDetailSheet(
                game = game,
                libraryEntry = libraryEntry,
                onDismiss = { selectedGameForDetails = null },
                onStatusChange = { newStatus ->
                    coroutineScope.launch {
                        AppContainer.libraryRepository.save(game, newStatus)
                        if (newStatus == LibraryStatus.COMPLETED) {
                            celebrationData = Pair(game, libraryEntry?.playtimeHours ?: 0.0)
                        } else if (newStatus == LibraryStatus.PLAYING) {
                            showToast("▶ Now Playing \"${game.name}\"!")
                        } else if (newStatus == LibraryStatus.BACKLOG) {
                            showToast("📥 Added \"${game.name}\" to Backlog!")
                        }
                    }
                },
                onProgressUpdate = { progress, playtime ->
                    coroutineScope.launch {
                        AppContainer.libraryRepository.updateProgress(game.id, progress, playtime)
                        if (progress >= 100) {
                            celebrationData = Pair(game, playtime)
                        }
                    }
                },
                onRemoveFromLibrary = {
                    selectedGameForDetails = null
                    gameToRemove = game
                },
                onOpenShareCard = { g -> gameForShareCard = g },
                onSimilarGameClick = { similarGame -> selectedGameForDetails = similarGame },
            )
        }

        // 2. Incoming Native Social Share Dialog (TikTok / Instagram / YouTube / Browser)
        val activeShareText = pendingShareText ?: if (manualSocialPasteActive) "" else null
        if (activeShareText != null) {
            QuickShareImportDialog(
                rawSharedText = activeShareText,
                libraryGames = libraryGames,
                onDismiss = {
                    ShareIntentHolder.consumeShare()
                    manualSocialPasteActive = false
                },
                onSaveGame = { game, status ->
                    coroutineScope.launch {
                        AppContainer.libraryRepository.save(game, status)
                        showToast("✓ Added \"${game.name}\" to ${status.name}")
                    }
                },
                onGameClick = { game -> selectedGameForDetails = game },
            )
        }

        // 3. Backlog Roulette ("What Should I Play Next?")
        if (showRouletteDialog) {
            val backlogEntries = libraryGames.filter { it.status == LibraryStatus.BACKLOG }
            BacklogRouletteDialog(
                backlogGames = backlogEntries,
                onDismiss = { showRouletteDialog = false },
                onStartPlaying = { entry ->
                    coroutineScope.launch {
                        AppContainer.libraryRepository.save(entry.toGame(), LibraryStatus.PLAYING)
                        showToast("▶ Started playing \"${entry.name}\"!")
                    }
                },
            )
        }

        // 4. Quest Completion Celebration Dialog
        celebrationData?.let { (game, playtime) ->
            CompletionCelebrationDialog(
                game = game,
                playtimeHours = playtime,
                onDismiss = { celebrationData = null },
                onRateGame = { rating ->
                    coroutineScope.launch {
                        AppContainer.libraryRepository.updateRating(game.id, rating)
                        showToast("★ Rated \"${game.name}\" $rating/5 Stars!")
                    }
                },
                onShareTrophy = { g -> gameForShareCard = g },
            )
        }

        // 5. Gamer Share Card Dialog
        gameForShareCard?.let { game ->
            ShareCardDialog(
                game = game,
                isPro = subscriptionState.isPro,
                onDismiss = { gameForShareCard = null },
                onOpenPaywall = {
                                    val handled = AppContainer.subscriptionRepository.showPaywall()
                                    if (!handled) {
                                        showProPaywallDialog = true
                                    }
                                },
            )
        }

        // 6. Meaningful Confirmation Warning Dialog for Removing Games
        gameToRemove?.let { game ->
            RemoveGameConfirmationDialog(
                game = game,
                onDismiss = { gameToRemove = null },
                onConfirmRemove = {
                    coroutineScope.launch {
                        AppContainer.libraryRepository.remove(game.id)
                        if (selectedGameForDetails?.id == game.id) {
                            selectedGameForDetails = null
                        }
                        showToast("✕ Removed \"${game.name}\" from Library")
                    }
                },
            )
        }

        // 7. Obsidian PRO RevenueCat Paywall Dialog
        if (showProPaywallDialog) {
            ObsidianProPaywallDialog(
                isPro = subscriptionState.isPro,
                dynamicPrices = subscriptionState.dynamicPrices,
                onDismiss = { showProPaywallDialog = false },
                onPurchasePlan = { plan ->
                    coroutineScope.launch {
                        val result = AppContainer.subscriptionRepository.purchasePlan(plan)
                        result.onSuccess {
                            showToast("👑 Welcome to Obsidian PRO!")
                        }.onFailure { err ->
                            val msg = err.message ?: "Purchase failed"
                            if (!msg.contains("cancelled", ignoreCase = true)) {
                                showToast(msg)
                            }
                        }
                    }
                },
                onRestorePurchases = {
                    coroutineScope.launch {
                        AppContainer.subscriptionRepository.restorePurchases()
                        showToast("Purchases restored successfully")
                    }
                },
                onToggleJudgePro = { enabled ->
                    coroutineScope.launch {
                        AppContainer.subscriptionRepository.setJudgeDemoPro(enabled)
                        showToast(if (enabled) "🎁 Judges Mode: PRO Unlocked!" else "Judges Mode: Pro Disabled")
                    }
                },
            )
        }

        // 8. 2026 Gaming Wrapped Social Story Card Modal
        if (showWrappedDialog) {
            com.obsidian.shipathon.ui.wrapped.GamingWrappedDialog(
                libraryGames = libraryGames,
                isPro = subscriptionState.isPro,
                onDismiss = { showWrappedDialog = false },
                onOpenPaywall = {
                    showWrappedDialog = false
                    showProPaywallDialog = true
                },
            )
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ObsidianTopBar(title: String, showLogo: Boolean) {
    Column {
        TopAppBar(
            title = {
                if (showLogo) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = GamingTextPrimary, fontWeight = FontWeight.Bold)) {
                                append("Obsidian")
                            }
                            withStyle(SpanStyle(color = GamingBlue, fontWeight = FontWeight.ExtraBold)) {
                                append("Play")
                            }
                        },
                        fontSize = 22.sp,
                        letterSpacing = (-0.5).sp,
                    )
                } else {
                    Text(
                        text = title,
                        color = GamingTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        letterSpacing = (-0.3).sp,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = GamingBackground,
            ),
        )

        // Subtle specular glow separator under topbar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(HeaderGlowBrush),
        )
    }
}

// ── Console-Grade Floating Dock Navigation Bar ────────────────────────────────

@Composable
private fun ObsidianNavBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(GamingBackground)
            .navigationBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 22.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Floating glassmorphic dock pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .background(GamingCardElevated)
                .border(1.2.dp, GlassBorderBrush, RoundedCornerShape(28.dp))
                .padding(horizontal = 6.dp, vertical = 6.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    DockNavItem(
                        tab = tab,
                        isSelected = isSelected,
                        onClick = { onTabSelected(tab) },
                    )
                }
            }
        }
    }
}

@Composable
private fun DockNavItem(
    tab: AppTab,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val icon = when (tab) {
        AppTab.Home -> ObsidianIcons.Home
        AppTab.Library -> ObsidianIcons.Gamepad
        AppTab.Search -> ObsidianIcons.Search
        AppTab.Profile -> ObsidianIcons.Profile
    }

    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.06f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "NavScale_${tab.name}",
    )

    Box(
        modifier = Modifier
            .scale(animatedScale)
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (isSelected) GamingBlue.copy(alpha = 0.22f)
                else Color.Transparent
            )
            .then(
                if (isSelected) Modifier.border(1.dp, GamingBlue.copy(alpha = 0.6f), RoundedCornerShape(22.dp))
                else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 18.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = tab.title,
                modifier = Modifier.size(21.dp),
                tint = if (isSelected) GamingCyan else GamingTextSecondary.copy(alpha = 0.7f),
            )

            // Neon glowing active capsule indicator
            Box(
                modifier = Modifier
                    .size(width = 12.dp, height = 3.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(
                        if (isSelected) GamingCyan
                        else Color.Transparent
                    ),
            )
        }
    }
}