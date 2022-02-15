package com.example.networkproject.data

import com.example.networkproject.annotation.IoDispatcher
import com.example.networkproject.data.database.dao.EmployeeDao
import com.example.networkproject.data.database.entities.Employee
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeeLocalDatasource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val employeeDao: EmployeeDao
) : EmployeesLocalApi {
    override suspend fun getEmployees(): Flow<List<Employee>> = withContext(ioDispatcher) {
        employeeDao.getEmployeesDistinctUntilChanged()
    }

    override suspend fun saveEmployees(vararg employees: Employee) {
        employeeDao.insertEmployees(*employees)
    }

    override suspend fun deleteAllEmployees() {
        employeeDao.deleteAllEmployees()
    }
}

interface EmployeesLocalApi {
    suspend fun getEmployees(): Flow<List<Employee>>
    suspend fun saveEmployees(vararg employees: Employee)
    suspend fun deleteAllEmployees()
}
