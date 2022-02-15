package com.example.networkproject.data

import com.example.networkproject.annotation.IoDispatcher
import com.example.networkproject.data.database.entities.Employee
import com.example.networkproject.data.network.ConnectivityService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface EmployeeRepository {
    suspend fun getEmployees(): Flow<List<Employee>>
    suspend fun insertEmployee(employee: Employee)
}

@ExperimentalCoroutinesApi
class EmployeeRepositoryImpl @Inject constructor(
    private val employeeRemoteDataSource: EmployeeRemoteDataSource,
    private val employeeLocalDatasource: EmployeeLocalDatasource,
    private val connectivityService: ConnectivityService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : EmployeeRepository {

    override suspend fun getEmployees(): Flow<List<Employee>> {
        val isNetworkActive = connectivityService.isNetworkConnected()
        return if (isNetworkActive) {
            employeeLocalDatasource.deleteAllEmployees()
            employeeRemoteDataSource.fetchEmployees().flatMapLatest {
                val smallerEmployeeList = it.take(3)
                employeeLocalDatasource.saveEmployees(*smallerEmployeeList.toTypedArray())
                employeeLocalDatasource.getEmployees()
            }
        } else {
            employeeLocalDatasource.getEmployees()
        }
    }

    override suspend fun insertEmployee(employee: Employee) = withContext(ioDispatcher) {
        employeeLocalDatasource.saveEmployees(employee)
    }
}
