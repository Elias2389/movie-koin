package com.arivas.moviesappkotlin.application

import android.app.Application
import com.arivas.moviesappkotlin.di.module.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BaseApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BaseApp)
            androidFileProperties()
            modules(applicationModule)
        }

    }

}