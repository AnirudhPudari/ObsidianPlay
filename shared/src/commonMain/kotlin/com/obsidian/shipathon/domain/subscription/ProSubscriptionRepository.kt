package com.obsidian.shipathon.domain.subscription

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class SubscriptionPlan(
    val id: String,
    val title: String,
    val priceDisplay: String,
    val periodDisplay: String,
    val subtitle: String,
    val isBestValue: Boolean = false,
    val hasTrial: Boolean = false,
) {
    ANNUAL(
        id = "obsidian_pro_annual",
        title = "Annual Pass",
        priceDisplay = "$19.99",
        periodDisplay = "billed annually",
        subtitle = "Includes 7-Day Free Trial",
        isBestValue = true,
        hasTrial = true,
    ),
    MONTHLY(
        id = "obsidian_pro_monthly",
        title = "Monthly Pass",
        priceDisplay = "$2.99",
        periodDisplay = "billed monthly",
        subtitle = "Cancel anytime",
        isBestValue = false,
        hasTrial = false,
    ),
    LIFETIME(
        id = "obsidian_pro_lifetime",
        title = "Lifetime Pass",
        priceDisplay = "$49.99",
        periodDisplay = "one-time payment",
        subtitle = "Pay once • VIP forever",
        isBestValue = false,
        hasTrial = false,
    ),
}

data class SubscriptionState(
    val isPro: Boolean = false,
    val activePlan: SubscriptionPlan? = null,
    val expiresAt: Long? = null,
    val dynamicPrices: Map<SubscriptionPlan, String> = emptyMap(),
)

interface ProSubscriptionRepository {
    val subscriptionState: StateFlow<SubscriptionState>

    suspend fun purchasePlan(plan: SubscriptionPlan): Result<Boolean>
    suspend fun restorePurchases(): Result<Boolean>
    suspend fun setJudgeDemoPro(enabled: Boolean)
    fun showPaywall(): Boolean = false
}

class InMemoryProSubscriptionRepository : ProSubscriptionRepository {
    private val _state = MutableStateFlow(SubscriptionState(isPro = false))
    override val subscriptionState: StateFlow<SubscriptionState> = _state.asStateFlow()

    override suspend fun purchasePlan(plan: SubscriptionPlan): Result<Boolean> {
        _state.value = SubscriptionState(
            isPro = true,
            activePlan = plan,
        )
        return Result.success(true)
    }

    override suspend fun restorePurchases(): Result<Boolean> {
        return Result.success(_state.value.isPro)
    }

    override suspend fun setJudgeDemoPro(enabled: Boolean) {
        _state.value = SubscriptionState(
            isPro = enabled,
            activePlan = if (enabled) SubscriptionPlan.ANNUAL else null,
        )
    }
}
