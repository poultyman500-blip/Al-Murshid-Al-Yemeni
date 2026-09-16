package com.almurshid.yemeni

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

class AlMurshidApp : Application() {
    override fun onCreate() {
        super.onCreate()

        runCatching {
            FirebaseApp.initializeApp(this)
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }
    }
}
