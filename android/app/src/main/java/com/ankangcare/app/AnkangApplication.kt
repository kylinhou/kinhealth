package com.ankangcare.app

import android.app.Application
import com.ankangcare.app.data.AnkangDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AnkangApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        AnkangDatabase.getDatabase(this, applicationScope)
    }

    override fun onCreate() {
        super.onCreate()
    }
}
