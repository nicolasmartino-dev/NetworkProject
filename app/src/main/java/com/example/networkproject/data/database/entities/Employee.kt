package com.example.networkproject.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmployeeData(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "email") val email: String ?,
)
