package com.winsontan520.mobiledatausage.di

import com.winsontan520.mobiledatausage.data.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single { AppDatabase.buildDatabase(androidContext()) }
    factory { get<AppDatabase>().recordDao() }
}