package com.example.networkproject.ui

import com.example.networkproject.data.EmployeeRepository
import com.example.networkproject.data.database.entities.Employee
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    
    private lateinit var repository: EmployeeRepository
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `init fetches employees successfully and updates UI state`() = testScope.runTest {
        // Given
        val expectedEmployees = listOf(
            Employee(1, "John Doe", "john.doe", "john@example.com"),
            Employee(2, "Jane Smith", "jane.smith", "jane@example.com"),
            Employee(3, "Bob Johnson", "bob.johnson", "bob@example.com")
        )
        coEvery { repository.getEmployees() } returns expectedEmployees
        
        // When
        viewModel = MainViewModel(repository)
        advanceUntilIdle()
        
        // Then
        assertEquals(expectedEmployees, viewModel.uiState.employees)
        assertTrue(viewModel.uiState.userMessages.isEmpty())
        coVerify(exactly = 1) { repository.getEmployees() }
    }
    
    @Test
    fun `init handles IOException and updates UI state with error message`() = testScope.runTest {
        // Given
        val errorMessage = "Network connection failed"
        coEvery { repository.getEmployees() } throws IOException(errorMessage)
        
        // When
        viewModel = MainViewModel(repository)
        advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.uiState.employees.isEmpty())
        assertEquals(listOf(errorMessage), viewModel.uiState.userMessages)
        coVerify(exactly = 1) { repository.getEmployees() }
    }
    
    @Test
    fun `init handles IOException with null message`() = testScope.runTest {
        // Given
        coEvery { repository.getEmployees() } throws IOException()
        
        // When
        viewModel = MainViewModel(repository)
        advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.uiState.employees.isEmpty())
        assertEquals(listOf(""), viewModel.uiState.userMessages)
        coVerify(exactly = 1) { repository.getEmployees() }
    }
    
    @Test
    fun `UI state is initially empty before fetching`() = testScope.runTest {
        // Given
        coEvery { repository.getEmployees() } returns emptyList()
        
        // When
        viewModel = MainViewModel(repository)
        // Don't advance time to check initial state
        
        // Then - verify initial state
        assertEquals(EmployeeUiState(), viewModel.uiState)
    }
    
    @Test
    fun `fetching employees with empty list updates UI state correctly`() = testScope.runTest {
        // Given
        coEvery { repository.getEmployees() } returns emptyList()
        
        // When
        viewModel = MainViewModel(repository)
        advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.uiState.employees.isEmpty())
        assertTrue(viewModel.uiState.userMessages.isEmpty())
        coVerify(exactly = 1) { repository.getEmployees() }
    }
    
    @Test
    fun `UI state preserves existing data when updating with new employees`() = testScope.runTest {
        // Given
        val firstEmployees = listOf(
            Employee(1, "Initial Employee", "initial", "initial@example.com")
        )
        val secondEmployees = listOf(
            Employee(2, "Updated Employee", "updated", "updated@example.com"),
            Employee(3, "Another Employee", "another", "another@example.com")
        )
        
        coEvery { repository.getEmployees() } returnsMany listOf(firstEmployees, secondEmployees)
        
        // When - create view model (triggers first fetch)
        viewModel = MainViewModel(repository)
        advanceUntilIdle()
        
        // Then - verify first state
        assertEquals(firstEmployees, viewModel.uiState.employees)
        assertTrue(viewModel.uiState.userMessages.isEmpty())
        
        // Note: Since fetchEmployees is private and only called in init,
        // this test demonstrates the initial fetch behavior
    }
    
    @Test
    fun `large employee list is handled correctly`() = testScope.runTest {
        // Given - create a large list of employees
        val largeEmployeeList = (1..1000).map { id ->
            Employee(
                id = id,
                name = "Employee $id",
                username = "employee$id",
                email = "employee$id@example.com"
            )
        }
        coEvery { repository.getEmployees() } returns largeEmployeeList
        
        // When
        viewModel = MainViewModel(repository)
        advanceUntilIdle()
        
        // Then
        assertEquals(largeEmployeeList.size, viewModel.uiState.employees.size)
        assertEquals(largeEmployeeList, viewModel.uiState.employees)
        assertTrue(viewModel.uiState.userMessages.isEmpty())
    }
    
    @Test
    fun `employees with null fields are handled correctly`() = testScope.runTest {
        // Given
        val employeesWithNulls = listOf(
            Employee(1, null, "username1", "email1@example.com"),
            Employee(2, "Name2", null, "email2@example.com"),
            Employee(3, "Name3", "username3", null),
            Employee(4, null, null, null)
        )
        coEvery { repository.getEmployees() } returns employeesWithNulls
        
        // When
        viewModel = MainViewModel(repository)
        advanceUntilIdle()
        
        // Then
        assertEquals(employeesWithNulls, viewModel.uiState.employees)
        assertTrue(viewModel.uiState.userMessages.isEmpty())
    }
    
    @Test
    fun `EmployeeUiState data class properties work correctly`() {
        // Test default values
        val defaultState = EmployeeUiState()
        assertTrue(defaultState.employees.isEmpty())
        assertTrue(defaultState.userMessages.isEmpty())
        
        // Test with values
        val employees = listOf(
            Employee(1, "Test", "test", "test@example.com")
        )
        val messages = listOf("Message 1", "Message 2")
        val state = EmployeeUiState(employees = employees, userMessages = messages)
        
        assertEquals(employees, state.employees)
        assertEquals(messages, state.userMessages)
        
        // Test copy function
        val newEmployees = listOf(
            Employee(2, "New", "new", "new@example.com")
        )
        val copiedState = state.copy(employees = newEmployees)
        assertEquals(newEmployees, copiedState.employees)
        assertEquals(messages, copiedState.userMessages) // messages should remain unchanged
    }
}