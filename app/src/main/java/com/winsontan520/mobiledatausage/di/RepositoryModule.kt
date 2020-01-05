package com.winsontan520.mobiledatausage.di

import com.winsontan520.mobiledatausage.data.repository.AppDispatchers
import com.winsontan520.mobiledatausage.data.repository.MobileDataUsageRepository
import com.winsontan520.mobiledatausage.data.repository.MobileDataUsageRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {
    factory { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
    factory { MobileDataUsageRepositoryImpl(get(), get()) as MobileDataUsageRepository }
}