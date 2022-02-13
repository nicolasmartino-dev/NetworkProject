package com.example.networkproject.data

import com.example.networkproject.annotation.ApplicationScope
import com.example.networkproject.data.network.Employee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeeRepository @Inject constructor(
    private val employeeRemoteDataSource: EmployeeRemoteDataSource,
    @ApplicationScope private val externalScope: CoroutineScope
) {

    // Mutex to make writes to cached values thread-safe.
    private val latestEmployeesMutex = Mutex()

    // Cache of the latest employees got from the network.
    private var latestEmployees: List<Employee> = emptyList()

    suspend fun getEmployees(refresh: Boolean = false): List<Employee> {
        if (refresh || latestEmployees.isEmpty()) {
            // Thread-safe write to latestNews
            withContext(externalScope.coroutineContext) {
                employeeRemoteDataSource.fetchEmployees().also { networkResult ->
                    // Thread-safe write to latestNews
                    latestEmployeesMutex.withLock {
                        latestEmployees = networkResult
                    }
                }

            }
        }

        return latestEmployeesMutex.withLock { this.latestEmployees }
    }
}
