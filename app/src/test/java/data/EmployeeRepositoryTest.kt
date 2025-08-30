package data

import com.example.networkproject.data.EmployeeLocalDatasource
import com.example.networkproject.data.EmployeeRemoteDataSource
import com.example.networkproject.data.EmployeeRepository
import com.example.networkproject.data.EmployeeRepositoryImpl
import com.example.networkproject.data.database.entities.Employee
import com.example.networkproject.data.network.ConnectivityService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import test_shared.MainCoroutineRule
import test_shared.TestData
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class EmployeeRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var employeeRepository: EmployeeRepository

    @RelaxedMockK lateinit var mockEmployeeRemoteDataSource: EmployeeRemoteDataSource
    @RelaxedMockK lateinit var mockEmployeeLocalDatasource: EmployeeLocalDatasource
    @RelaxedMockK lateinit var mockConnectivityService: ConnectivityService

    @Before
    fun beforeTesting() {
        MockKAnnotations.init(this)
        employeeRepository = EmployeeRepositoryImpl(
            mainCoroutineRule.testScope,
            mockEmployeeRemoteDataSource,
            mockEmployeeLocalDatasource,
            mockConnectivityService
        )
    }

    @Test
    fun getEmployee_withNetworkConnectivity_getEmployeeFromRemote(): Unit = runBlocking {
        every { mockConnectivityService.isNetworkConnected() } returns true
        coEvery { mockEmployeeRemoteDataSource.fetchEmployees() } returns TestData.employeeTestList
        val employees = employeeRepository.getEmployees()
        assertEquals(TestData.employeeTestList, employees)
        coVerify(exactly = 1) { mockEmployeeLocalDatasource.saveEmployees(*anyVararg()) }
    }

    @Test
    fun getEmployee_withoutNetworkConnectivity_getEmployeeFromLocal(): Unit = runBlocking {
        every { mockConnectivityService.isNetworkConnected() } returns false
        coEvery { mockEmployeeLocalDatasource.getEmployees() } returns TestData.employeeTestList
        val employees = employeeRepository.getEmployees()
        assertEquals(TestData.employeeTestList, employees)
        coVerify(exactly = 1) { mockEmployeeLocalDatasource.getEmployees() }
    }
}
