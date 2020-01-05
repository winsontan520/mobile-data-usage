package com.winsontan520.mobiledatausage.di

val appModule = listOf(
    createRemoteModule("https://data.gov.sg/"),
    localModule,
    repositoryModule,
    featureHomeModule
)