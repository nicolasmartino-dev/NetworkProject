package com.example.networkproject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.networkproject.data.Employee
import com.example.networkproject.ui.theme.NetworkProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NetworkProjectTheme {
                EmployeeContent()
            }
        }
    }

    @Composable
    fun EmployeeContent(model: MainViewModel = viewModel()) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(model.uiState.employees) {
                EmployeeListItem(it)
            }
        }
    }

    @Composable
    fun EmployeeListItem(employee: Employee) {
        Row {
            Column {
                Text(text = employee.name, style = typography.h6)
                Text(text = employee.username, style = typography.body1)
                Text(text = employee.email, style = typography.caption)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NetworkProjectTheme {
        Greeting("Android")
    }
}
