package com.example.networkproject.data

import com.example.networkproject.annotation.IoDispatcher
import com.example.networkproject.data.database.dao.EmployeeDao
import com.example.networkproject.data.database.entities.Employee
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeeLocalDatasource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val employeeDao: EmployeeDao
) : EmployeesLocalApi {
    override suspend fun getEmployees(): List<Employee> = withContext(ioDispatcher) {
        employeeDao.loadEmployees()
    }

    override suspend fun saveEmployees(vararg employees: Employee) {
        employeeDao.insertEmployees(*employees)
    }
}

interface EmployeesLocalApi {
    suspend fun getEmployees(): List<Employee>
    suspend fun saveEmployees(vararg employees: Employee)
}
