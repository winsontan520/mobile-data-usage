package com.winsontan520.mobiledatausage

import android.app.Application
import com.winsontan520.mobiledatausage.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    open fun configureDi() =
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(getAppModules())
        }

    open fun getAppModules() = appModule
}