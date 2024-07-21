package com.julien.bankmanagement.app

import android.app.Application
import com.julien.bankmanagement.database.AppDatabase

class App: Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}