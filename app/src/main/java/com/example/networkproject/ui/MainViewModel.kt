package com.example.networkproject.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networkproject.data.EmployeeRepository
import com.example.networkproject.data.database.entities.Employee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: EmployeeRepository,
) : ViewModel() {
    var uiState by mutableStateOf(EmployeeUiState())
        private set

    var formState by mutableStateOf(EmployeeFormState())
        private set

    private var fetchJob: Job? = null

    init {
        fetchEmployees()
    }

    private fun fetchEmployees() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            repository.getEmployees().collect { employees ->
                uiState = uiState.copy(employees = employees)
            }
        }
    }

    fun updateName(name: String) {
        formState = formState.copy(name = name)
        isFormValid()
    }

    fun updateUserName(userName: String) {
        formState = formState.copy(userName = userName)
        isFormValid()
    }

    fun updateEmail(email: String) {
        formState = formState.copy(email = email)
        isFormValid()
    }

    fun postForm() {
        viewModelScope.launch {
            repository.insertEmployee(
                Employee(
                    name = formState.name,
                    username = formState.userName,
                    email = formState.email
                )
            )
        }
    }

    private fun isFormValid() {
        val isValid = formState.email.isNotEmpty() and formState.name.isNotEmpty() and formState.userName.isNotEmpty()
        formState = formState.copy(isFormValid = isValid)
    }
}

data class EmployeeUiState(
    val employees: List<Employee> = emptyList(),
    val userMessages: List<String> = emptyList()
)

data class EmployeeFormState(
    val name: String = "",
    val userName: String = "",
    val email: String = "",
    val isFormValid: Boolean = false
)
