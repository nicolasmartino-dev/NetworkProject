package com.example.networkproject.di.module.data

import android.content.Context
import androidx.room.Room
import com.example.networkproject.data.database.AppDatabase
import com.example.networkproject.data.database.dao.EmployeeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDataModule {
        @Provides
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java)
                .build()
        }

        @Provides
        fun provideEmployeeDao(appDatabase: AppDatabase): EmployeeDao {
            return appDatabase.employeeDao()
        }
}
