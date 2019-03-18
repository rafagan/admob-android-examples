package com.drops.admobexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    lateinit var banner : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAds()
        confAdsCallbacks()
    }

    private fun initAds() {
        MobileAds.initialize(this, getString(R.string.admob_id))

        banner = findViewById(R.id.banner)
        val adRequest = AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice("810C89CA50E405EFB0CF693F49E40558")
            .build()
        banner.loadAd(adRequest)
    }

    private fun confAdsCallbacks() {
        banner.adListener = object: AdListener() {
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
            }
        }
    }
}
