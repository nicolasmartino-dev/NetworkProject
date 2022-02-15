package com.example.networkproject.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["email"], unique = true)])
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String?,
    val username: String?,
    val email: String,
)
