package com.example.networkproject.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.networkproject.data.database.entities.Employee

@Dao
interface EmployeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(vararg users: Employee)

    @Update
    suspend fun updateEmployees(vararg users: Employee)

    @Delete
    suspend fun deleteEmployees(vararg users: Employee)

    @Query("SELECT * FROM employee")
    suspend fun loadEmployees(): List<Employee>

    @Query("SELECT * FROM employee WHERE id = :id")
    suspend fun loadEmployeeById(id: Int): Employee

    @Query("SELECT * from employee WHERE name = :name")
    suspend fun loadEmployeeByName(name: String): Employee
}
