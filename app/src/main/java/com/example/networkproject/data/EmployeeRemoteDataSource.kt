package com.example.networkproject.data

import com.example.networkproject.data.database.entities.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EmployeeRemoteDataSource @Inject constructor(
    private val employeesRemoteApi: EmployeesRemoteApi,
) {
    suspend fun fetchEmployees(): Flow<List<Employee>> = flow {
        val employees = employeesRemoteApi.fetchEmployees()
        emit(employees)
    }
}
