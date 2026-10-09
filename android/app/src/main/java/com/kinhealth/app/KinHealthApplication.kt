package com.kinhealth.app

import android.app.Application
import com.kinhealth.app.data.KinHealthDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class KinHealthApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        KinHealthDatabase.getDatabase(this, applicationScope)
    }

    override fun onCreate() {
        super.onCreate()
    }
}

