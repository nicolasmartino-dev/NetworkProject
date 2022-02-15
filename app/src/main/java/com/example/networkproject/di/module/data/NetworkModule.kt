package com.example.networkproject.di.module.data

import android.content.Context
import android.net.ConnectivityManager
import com.example.networkproject.data.network.ConnectivityService
import com.example.networkproject.data.network.ConnectivityServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun bindConnectivityService(connectivityService: ConnectivityServiceImpl): ConnectivityService

    companion object {
        @Provides
        fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}
