package com.example.networkproject.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networkproject.data.Employee
import com.example.networkproject.data.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: EmployeeRepository,
) : ViewModel() {
    var uiState by mutableStateOf(EmployeeUiState())
        private set

    private var fetchJob: Job? = null

    init {
        fetchEmployees()
    }

    private fun fetchEmployees() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            uiState = try {
                val employeeItems = repository.getEmployees()
                uiState.copy(employees = employeeItems)
            } catch (ioe: IOException) {
                // Handle the error and notify the UI when appropriate.
                val message = ioe.localizedMessage
                uiState.copy(userMessages = listOf(message.orEmpty()))
            }
        }
    }
}

data class EmployeeUiState(
    val employees: List<Employee> = emptyList(),
    val userMessages: List<String> = emptyList()
)
