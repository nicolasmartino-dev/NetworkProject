package com.example.networkproject.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.networkproject.data.database.entities.EmployeeData

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employeedata")
    fun getAll(): List<EmployeeData>

    @Query("SELECT * FROM employeedata WHERE uid IN (:employeeIds)")
    fun loadAllByIds(employeeIds: IntArray): List<EmployeeData>

    @Query("SELECT * FROM employeedata WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): EmployeeData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg employees: EmployeeData)

    @Update
    fun updateEmployees(vararg employee: EmployeeData)

    @Delete
    fun delete(employee: EmployeeData)
}
