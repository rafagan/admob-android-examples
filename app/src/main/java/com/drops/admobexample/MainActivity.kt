package com.drops.admobexample

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.adcolony.sdk.AdColony
import com.adcolony.sdk.AdColonyAppOptions
import com.adcolony.sdk.AdColonyInterstitial
import com.adcolony.sdk.AdColonyInterstitialListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


class MainActivity : AppCompatActivity() {
    private lateinit var banner : AdView
    private lateinit var interstitialAd: InterstitialAd
    private lateinit var adColonyInterstitialListener: AdColonyInterstitialListener
    private lateinit var rewardedAd: RewardedAd


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAdMob()
        confAdMobCallbacks()

        initAdColony()
    }

    private fun initAdMob() {
        MobileAds.initialize(this, getString(R.string.admob_id))

        val adRequest = AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice("810C89CA50E405EFB0CF693F49E40558")
            .build()

        banner = findViewById(R.id.banner)
        banner.loadAd(adRequest)

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.interstitial_id)
        interstitialAd.loadAd(adRequest)

        loadRewarded()
    }

    private fun initAdColony() {
        val options = AdColonyAppOptions()
        options.keepScreenOn = true
        options.gdprRequired = true
        options.gdprConsentString = "1"

        AdColony.configure(
            this,
            options,
            getString(R.string.adcolony_id),
            getString(R.string.adcolony_zone_interstitial))

        AdColony.setAppOptions(options)

        adColonyInterstitialListener = object : AdColonyInterstitialListener() {
            override fun onRequestFilled(ad: AdColonyInterstitial) {
                ad.show()
            }
        }
    }

    private fun loadInterstitial() {
        val adRequest = AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice("810C89CA50E405EFB0CF693F49E40558")
            .build()

        interstitialAd.loadAd(adRequest)
    }

    private fun loadRewarded() {
        val adRequest = AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice("810C89CA50E405EFB0CF693F49E40558")
            .build()

        rewardedAd = RewardedAd(this, getString(R.string.rewarded_id))

        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                Log.d("ads", "onRewardedAdLoaded")
            }

            override fun onRewardedAdFailedToLoad(errorCode: Int) {
                val errorType = when(errorCode) {
                    AdRequest.ERROR_CODE_INTERNAL_ERROR -> "ERROR_CODE_INTERNAL_ERROR"
                    AdRequest.ERROR_CODE_INVALID_REQUEST -> "ERROR_CODE_INVALID_REQUEST"
                    AdRequest.ERROR_CODE_NETWORK_ERROR -> "ERROR_CODE_NETWORK_ERROR"
                    AdRequest.ERROR_CODE_NO_FILL -> "ERROR_CODE_NO_FILL"
                    else -> "Erro desconhecido"
                }
                Log.d("ads", "onRewardedAdFailedToLoad: $errorType")
            }
        }
        rewardedAd.loadAd(adRequest, adLoadCallback)
    }

    fun showInterstitial(v: View) {
//        AdColony.requestInterstitial(
//            getString(R.string.adcolony_zone_interstitial),
//            adColonyInterstitialListener)

        if (interstitialAd.isLoaded) {
            interstitialAd.show()
        } else {
            Log.d("ads", "The interstitial wasn't loaded yet.")
            loadInterstitial()
        }
    }

    fun showRewarded(v: View) {
        if (rewardedAd.isLoaded) {
            val adCallback = object : RewardedAdCallback() {
                override fun onRewardedAdOpened() {
                    Log.d("ads", "onRewardedAdOpened")
                }

                override fun onRewardedAdClosed() {
                    Log.d("ads", "onRewardedAdClosed")
                    loadRewarded()
                }

                override fun onUserEarnedReward(reward: RewardItem) {
                    Log.d("ads", "onUserEarnedReward")
                }

                override fun onRewardedAdFailedToShow(errorCode: Int) {
                    val errorType = when(errorCode) {
                        RewardedAdCallback.ERROR_CODE_INTERNAL_ERROR -> "ERROR_CODE_INTERNAL_ERROR"
                        RewardedAdCallback.ERROR_CODE_AD_REUSED -> "ERROR_CODE_AD_REUSED"
                        RewardedAdCallback.ERROR_CODE_NOT_READY -> "ERROR_CODE_NOT_READY"
                        RewardedAdCallback.ERROR_CODE_APP_NOT_FOREGROUND -> "ERROR_CODE_APP_NOT_FOREGROUND"
                        else -> "Erro desconhecido"
                    }
                    Log.d("ads", "onRewardedAdFailedToShow: $errorType")
                }
            }
            rewardedAd.show(this, adCallback)
        } else {
            Log.d("ads", "The rewarded ad wasn't loaded yet.")
        }
    }

    private fun confAdMobCallbacks() {
        val adListener = object: AdListener() {
            override fun onAdLoaded() {
                Log.d("ads", "onAdLoaded")
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                val errorType = when(errorCode) {
                    AdRequest.ERROR_CODE_INTERNAL_ERROR -> "ERROR_CODE_INTERNAL_ERROR"
                    AdRequest.ERROR_CODE_INVALID_REQUEST -> "ERROR_CODE_INVALID_REQUEST"
                    AdRequest.ERROR_CODE_NETWORK_ERROR -> "ERROR_CODE_NETWORK_ERROR"
                    AdRequest.ERROR_CODE_NO_FILL -> "ERROR_CODE_NO_FILL"
                    else -> "Erro desconhecido"
                }
                Log.d("ads", "onAdFailedToLoad: $errorType")
            }

            override fun onAdOpened() {
                Log.d("ads", "onAdOpened")
            }

            override fun onAdLeftApplication() {
                Log.d("ads", "onAdLeftApplication")
            }

            override fun onAdClosed() {
                Log.d("ads", "onAdClosed")
                loadInterstitial()
            }
        }

        banner.adListener = adListener
        interstitialAd.adListener = adListener
    }
}
