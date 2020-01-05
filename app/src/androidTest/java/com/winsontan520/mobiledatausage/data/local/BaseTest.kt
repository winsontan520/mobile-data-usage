package com.winsontan520.mobiledatausage.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
abstract class BaseTest : KoinTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    protected val database: AppDatabase by inject()

    @Before
    open fun setUp() {
        loadKoinModules(listOf(getLocalModuleTest()))
    }

    @After
    open fun tearDown() {
        database.close()
        stopKoin()
    }

    private fun getLocalModuleTest() = module(override = true) {
        single {
            Room.inMemoryDatabaseBuilder(
                get(),
                AppDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()
        }
        factory { get<AppDatabase>().recordDao() }
    }
}