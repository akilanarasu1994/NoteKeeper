package com.akilan.notekeeper

import android.app.Application
import timber.log.Timber

class NoteKeeperApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}