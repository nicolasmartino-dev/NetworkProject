package com.example.networkproject.data

import com.example.networkproject.annotation.ApplicationScope
import com.example.networkproject.data.database.entities.Employee
import com.example.networkproject.data.network.ConnectivityService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface EmployeeRepository {
    suspend fun getEmployees(refresh: Boolean = false): List<Employee>
}

class EmployeeRepositoryImpl @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val employeeRemoteDataSource: EmployeeRemoteDataSource,
    private val employeeLocalDatasource: EmployeeLocalDatasource,
    private val connectivityService: ConnectivityService
): EmployeeRepository {

    // Mutex to make writes to cached values thread-safe.
    private val latestEmployeesMutex = Mutex()

    // Cache of the latest employees got from the network.
    private var latestEmployees: List<Employee> = emptyList()

    override suspend fun getEmployees(refresh: Boolean): List<Employee> {
        if (refresh || latestEmployees.isEmpty()) {
            val isNetworkActive = connectivityService.isNetworkConnected()
            // Thread-safe write to latestNews
            withContext(externalScope.coroutineContext) {
                // Thread-safe write to latestEmployees
                latestEmployeesMutex.withLock {
                    if (isNetworkActive) {
                        employeeRemoteDataSource.fetchEmployees().also { networkResult ->
                            latestEmployees = networkResult
                            employeeLocalDatasource.saveEmployees(*latestEmployees.toTypedArray())

                        }
                    } else {
                        latestEmployees = employeeLocalDatasource.getEmployees()
                    }
                }
            }
        }

        return latestEmployeesMutex.withLock { this.latestEmployees }
    }
}
