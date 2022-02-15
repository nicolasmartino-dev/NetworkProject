package com.example.networkproject.data

import com.example.networkproject.annotation.IoDispatcher
import com.example.networkproject.data.database.entities.Employee
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeeRemoteDataSource @Inject constructor(
    private val employeesRemoteApi: EmployeesRemoteApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchEmployees(): List<Employee> =
    // Move the execution to an IO-optimized thread since the ApiService
        // doesn't support coroutines and makes synchronous requests.
        withContext(ioDispatcher) {
            employeesRemoteApi.fetchEmployees()
        }
}
