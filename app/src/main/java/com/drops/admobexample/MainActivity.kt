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
        val adRequest = AdRequest.Builder().build()
        banner.loadAd(adRequest)
    }

    private fun confAdsCallbacks() {
        banner.adListener = object: AdListener() {
            override fun onAdLoaded() {
                Log.d("ads", "onAdLoaded")
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                Log.d("ads", "onAdFailedToLoad")
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
