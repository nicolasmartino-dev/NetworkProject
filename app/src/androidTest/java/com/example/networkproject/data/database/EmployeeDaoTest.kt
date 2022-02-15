package com.example.networkproject.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.networkproject.data.database.dao.EmployeeDao
import com.example.networkproject.data.database.entities.Employee
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import test_shared.TestData
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
class EmployeeDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: AppDatabase
    private lateinit var employeeDao: EmployeeDao

    @Before
    fun createDb() {
        hiltRule.inject()
        employeeDao = database.employeeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runBlocking {
        val employee: Employee = TestData.employeeTestList.first()
        employeeDao.insertEmployees(employee)

        val byName = employeeDao.loadEmployeeByName("Leanne Graham")
        assertThat(byName, equalTo(employee))
    }
}
