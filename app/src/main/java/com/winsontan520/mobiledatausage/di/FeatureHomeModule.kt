package com.winsontan520.mobiledatausage.di

import com.winsontan520.mobiledatausage.feature.home.HomeViewModel
import com.winsontan520.mobiledatausage.feature.home.domain.GetMobileDataUsageUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureHomeModule = module {
    factory { GetMobileDataUsageUseCase(get()) }
    viewModel { HomeViewModel(get(), get()) }
}