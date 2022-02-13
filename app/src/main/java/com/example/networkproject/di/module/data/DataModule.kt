package com.example.networkproject.di.module.data

import com.example.networkproject.data.EmployeesApi
import com.example.networkproject.data.EmployeesApiImpl
import com.example.networkproject.data.network.NetworkCallHelper
import com.example.networkproject.data.network.NetworkCallHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun provideEmployeesApi(employeesApi: EmployeesApiImpl): EmployeesApi

    @Binds
    abstract fun provideNetworkCallHelper(networkCallHelper: NetworkCallHelperImpl): NetworkCallHelper
}
