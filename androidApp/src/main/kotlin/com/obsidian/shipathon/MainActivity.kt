package com.obsidian.shipathon

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.obsidian.shipathon.billing.RevenueCatSubscriptionRepository
import com.obsidian.shipathon.data.local.db.AndroidAppContextHolder
import com.obsidian.shipathon.di.AppContainer
import com.obsidian.shipathon.domain.share.ShareIntentHolder
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.models.StoreTransaction
import com.revenuecat.purchases.ui.revenuecatui.PaywallDialog
import com.revenuecat.purchases.ui.revenuecatui.PaywallDialogOptions
import com.revenuecat.purchases.ui.revenuecatui.PaywallListener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Enforce bright white status bar and navigation bar icons against our dark cyber theme
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        super.onCreate(savedInstanceState)
        AndroidAppContextHolder.context = applicationContext

        // Initialize RevenueCat Purchases SDK
        try {
            Purchases.logLevel = LogLevel.DEBUG
            Purchases.configure(
                PurchasesConfiguration.Builder(
                    context = applicationContext,
                    apiKey = "goog_AjfbgDSzVmEVryFchFLYLMPqIah"
                ).build()
            )
            AppContainer.subscriptionRepository = RevenueCatSubscriptionRepository { this }
        } catch (_: Exception) {}

        handleIntent(intent)

        setContent {
            val paywallRequested by RevenueCatSubscriptionRepository.showRevenueCatPaywallFlow.collectAsState()

            App()

            if (paywallRequested) {
                PaywallDialog(
                    PaywallDialogOptions.Builder()
                        .setDismissRequest {
                            RevenueCatSubscriptionRepository.dismissRevenueCatPaywall()
                        }
                        .setListener(object : PaywallListener {
                            override fun onPurchaseCompleted(
                                customerInfo: CustomerInfo,
                                storeTransaction: StoreTransaction
                            ) {
                                RevenueCatSubscriptionRepository.onCustomerInfoUpdated(customerInfo)
                                RevenueCatSubscriptionRepository.dismissRevenueCatPaywall()
                            }

                            override fun onRestoreCompleted(customerInfo: CustomerInfo) {
                                RevenueCatSubscriptionRepository.onCustomerInfoUpdated(customerInfo)
                                RevenueCatSubscriptionRepository.dismissRevenueCatPaywall()
                            }
                        })
                        .build()
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND) {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                ?: intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString()
            if (!sharedText.isNullOrBlank()) {
                ShareIntentHolder.onShareReceived(sharedText)
            }
        }
    }
}