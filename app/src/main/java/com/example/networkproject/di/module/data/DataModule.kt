package com.example.networkproject.di.module.data

import com.example.networkproject.data.EmployeeRepository
import com.example.networkproject.data.EmployeeRepositoryImpl
import com.example.networkproject.data.EmployeesRemoteApi
import com.example.networkproject.data.EmployeesRemoteApiImpl
import com.example.networkproject.data.network.HttpUrlConnectionNetworkCallHelperImpl
import com.example.networkproject.data.network.NetworkCallHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun provideEmployeeRepository(employeeRepository: EmployeeRepositoryImpl): EmployeeRepository

    @Binds
    abstract fun provideEmployeesRemoteApi(employeesApi: EmployeesRemoteApiImpl): EmployeesRemoteApi

    @Binds
    abstract fun provideNetworkCallHelper(networkCallHelper: HttpUrlConnectionNetworkCallHelperImpl): NetworkCallHelper
}
