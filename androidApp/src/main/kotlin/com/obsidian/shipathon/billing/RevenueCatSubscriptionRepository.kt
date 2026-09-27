package com.obsidian.shipathon.billing

import android.app.Activity
import com.obsidian.shipathon.domain.subscription.ProSubscriptionRepository
import com.obsidian.shipathon.domain.subscription.SubscriptionPlan
import com.obsidian.shipathon.domain.subscription.SubscriptionState
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import com.revenuecat.purchases.interfaces.UpdatedCustomerInfoListener
import com.revenuecat.purchases.models.StoreTransaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RevenueCatSubscriptionRepository(
    private val activityProvider: () -> Activity?
) : ProSubscriptionRepository, UpdatedCustomerInfoListener {

    companion object {
        private val _showPaywallFlow = MutableStateFlow(false)
        val showRevenueCatPaywallFlow: StateFlow<Boolean> = _showPaywallFlow.asStateFlow()

        private var activeInstance: RevenueCatSubscriptionRepository? = null

        fun dismissRevenueCatPaywall() {
            _showPaywallFlow.value = false
        }

        fun onCustomerInfoUpdated(customerInfo: CustomerInfo) {
            activeInstance?.updateStateFromCustomerInfo(customerInfo)
        }
    }

    private val _state = MutableStateFlow(SubscriptionState(isPro = false))
    override val subscriptionState: StateFlow<SubscriptionState> = _state.asStateFlow()

    init {
        activeInstance = this
        try {
            if (Purchases.isConfigured) {
                Purchases.sharedInstance.updatedCustomerInfoListener = this
                Purchases.sharedInstance.getCustomerInfo(object : ReceiveCustomerInfoCallback {
                    override fun onReceived(customerInfo: CustomerInfo) {
                        updateStateFromCustomerInfo(customerInfo)
                    }

                    override fun onError(error: PurchasesError) {
                        // Keep current state
                    }
                })

                // Fetch dynamic localized pricing (e.g. ₹ INR in India, € in EU, $ in US)
                Purchases.sharedInstance.getOfferingsWith(
                    onError = {},
                    onSuccess = { offerings ->
                        val offering = offerings.current
                        val prices = mutableMapOf<SubscriptionPlan, String>()
                        offering?.annual?.product?.price?.formatted?.let { prices[SubscriptionPlan.ANNUAL] = it }
                        offering?.monthly?.product?.price?.formatted?.let { prices[SubscriptionPlan.MONTHLY] = it }
                        offering?.lifetime?.product?.price?.formatted?.let { prices[SubscriptionPlan.LIFETIME] = it }

                        if (prices.isNotEmpty()) {
                            _state.value = _state.value.copy(dynamicPrices = prices)
                        }
                    }
                )
            }
        } catch (_: Exception) {}
    }

    override fun onReceived(customerInfo: CustomerInfo) {
        updateStateFromCustomerInfo(customerInfo)
    }

    private fun updateStateFromCustomerInfo(customerInfo: CustomerInfo) {
        val proEntitlement = customerInfo.entitlements["pro"]
        val isPro = proEntitlement?.isActive == true
        val activePlan = when {
            proEntitlement?.productIdentifier?.contains("annual", ignoreCase = true) == true -> SubscriptionPlan.ANNUAL
            proEntitlement?.productIdentifier?.contains("monthly", ignoreCase = true) == true -> SubscriptionPlan.MONTHLY
            proEntitlement?.productIdentifier?.contains("lifetime", ignoreCase = true) == true -> SubscriptionPlan.LIFETIME
            isPro -> SubscriptionPlan.ANNUAL
            else -> null
        }
        _state.value = _state.value.copy(
            isPro = isPro,
            activePlan = activePlan,
        )
    }

    override suspend fun purchasePlan(plan: SubscriptionPlan): Result<Boolean> {
        val activity = activityProvider()
        if (!Purchases.isConfigured || activity == null) {
            return Result.failure(Exception("Purchases SDK is not configured"))
        }

        return suspendCancellableCoroutine<Result<Boolean>> { continuation ->
            Purchases.sharedInstance.getOfferingsWith(
                onError = { error ->
                    continuation.resume(Result.failure(Exception(error.message)))
                },
                onSuccess = { offerings ->
                    val offering = offerings.current
                    val pkg = when (plan) {
                        SubscriptionPlan.ANNUAL -> offering?.annual ?: offering?.availablePackages?.firstOrNull { it.product.id.contains("annual") }
                        SubscriptionPlan.MONTHLY -> offering?.monthly ?: offering?.availablePackages?.firstOrNull { it.product.id.contains("monthly") }
                        SubscriptionPlan.LIFETIME -> offering?.lifetime ?: offering?.availablePackages?.firstOrNull { it.product.id.contains("lifetime") }
                    } ?: offering?.availablePackages?.firstOrNull()

                    // Update dynamic prices cache
                    val prices = mutableMapOf<SubscriptionPlan, String>()
                    offering?.annual?.product?.price?.formatted?.let { prices[SubscriptionPlan.ANNUAL] = it }
                    offering?.monthly?.product?.price?.formatted?.let { prices[SubscriptionPlan.MONTHLY] = it }
                    offering?.lifetime?.product?.price?.formatted?.let { prices[SubscriptionPlan.LIFETIME] = it }
                    if (prices.isNotEmpty()) {
                        _state.value = _state.value.copy(dynamicPrices = prices)
                    }

                    if (pkg != null) {
                        val params = PurchaseParams.Builder(activity, pkg).build()
                        Purchases.sharedInstance.purchase(
                            params,
                            object : PurchaseCallback {
                                override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                                    updateStateFromCustomerInfo(customerInfo)
                                    val isNowPro = _state.value.isPro
                                    if (isNowPro) {
                                        continuation.resume(Result.success(true))
                                    } else {
                                        continuation.resume(Result.failure(Exception("Purchase completed but PRO entitlement is not active")))
                                    }
                                }

                                override fun onError(error: PurchasesError, userCancelled: Boolean) {
                                    if (userCancelled) {
                                        continuation.resume(Result.failure(Exception("Purchase cancelled by user")))
                                    } else {
                                        continuation.resume(Result.failure(Exception(error.message)))
                                    }
                                }
                            }
                        )
                    } else {
                        continuation.resume(Result.failure(Exception("No active Google Play store package found for ${plan.title}")))
                    }
                }
            )
        }
    }

    override suspend fun restorePurchases(): Result<Boolean> {
        if (!Purchases.isConfigured) {
            return Result.success(_state.value.isPro)
        }

        return suspendCancellableCoroutine<Result<Boolean>> { continuation ->
            Purchases.sharedInstance.restorePurchases(object : ReceiveCustomerInfoCallback {
                override fun onReceived(customerInfo: CustomerInfo) {
                    updateStateFromCustomerInfo(customerInfo)
                    continuation.resume(Result.success(_state.value.isPro))
                }

                override fun onError(error: PurchasesError) {
                    continuation.resume(Result.success(_state.value.isPro))
                }
            })
        }
    }

    override suspend fun setJudgeDemoPro(enabled: Boolean) {
        _state.value = _state.value.copy(
            isPro = enabled,
            activePlan = if (enabled) SubscriptionPlan.ANNUAL else null,
        )
    }

    override fun showPaywall(): Boolean {
        // Return false to present the custom Obsidian dark theme paywall dialog
        return false
    }
}
