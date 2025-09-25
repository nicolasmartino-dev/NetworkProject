package com.example.networkproject.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.networkproject.data.database.dao.EmployeeDao
import com.example.networkproject.data.database.entities.Employee

@Database(entities = [Employee::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}
