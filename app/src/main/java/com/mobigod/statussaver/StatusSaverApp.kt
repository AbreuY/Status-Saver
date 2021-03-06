package com.mobigod.statussaver

import android.app.Application
import android.content.Context
import android.util.Log

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.mobigod.statussaver.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import com.google.android.gms.ads.MobileAds
import cafe.adriel.androidaudioconverter.callback.ILoadCallback
import cafe.adriel.androidaudioconverter.AndroidAudioConverter



class StatusSaverApp: MultiDexApplication(), HasAndroidInjector {
    private val TAG = StatusSaverApp::class.simpleName

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

         DaggerAppComponent
            .builder()
            .application(this)
            .build().inject(this)

        AndroidAudioConverter.load(this, object : ILoadCallback {
            override fun onSuccess() {
                // Great!
                Log.i(TAG, "FFMPEG Loaded successfully")
            }
            override fun onFailure(error: Exception) {
                Log.i(TAG, "FFMPEG ERR!!!!: ${error.message}")
                // FFmpeg is not supported by device
            }
        })

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return activityDispatchingAndroidInjector
    }

}