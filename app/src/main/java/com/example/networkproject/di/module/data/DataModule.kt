package com.example.networkproject.di.module.data

import android.content.Context
import androidx.room.Room
import com.example.networkproject.data.EmployeeRepository
import com.example.networkproject.data.EmployeeRepositoryImpl
import com.example.networkproject.data.EmployeesRemoteApi
import com.example.networkproject.data.EmployeesRemoteApiImpl
import com.example.networkproject.data.database.AppDatabase
import com.example.networkproject.data.database.dao.EmployeeDao
import com.example.networkproject.data.network.NetworkCallHelper
import com.example.networkproject.data.network.HttpUrlConnectionNetworkCallHelperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

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
