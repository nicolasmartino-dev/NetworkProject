package com.example.networkproject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.networkproject.data.database.entities.Employee
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
        Column(Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(model.uiState.employees) {
                    EmployeeListItem(it)
                }
            }
            EmployeeForm(
                nameText = model.formState.name,
                usernameText = model.formState.userName,
                emailText = model.formState.email,
                onNameChanged = { model.updateName(it) },
                onUsernameChanged = { model.updateUserName(it) },
                onEmailChanged = { model.updateEmail(it) },
                onPostForm = { model.postForm() },
                isFormValid = model.formState.isFormValid
            )
        }
    }

    @Composable
    fun EmployeeForm(
        nameText: String = "",
        usernameText: String = "",
        emailText: String = "",
        onNameChanged: (String) -> Unit = {},
        onUsernameChanged: (String) -> Unit = {},
        onEmailChanged: (String) -> Unit = {},
        onPostForm: () -> Unit = {},
        isFormValid: Boolean = false
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nameText,
                label = { Text("Name") },
                onValueChange = onNameChanged,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = usernameText,
                label = { Text("Username") },
                onValueChange = onUsernameChanged,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = emailText,
                label = { Text("Email") },
                onValueChange = onEmailChanged,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(4.dp))
            Button(enabled = isFormValid, onClick = { onPostForm() }) {
                Text(text = "Save")
            }
        }
    }

    @Composable
    fun EmployeeListItem(employee: Employee) {
        Row {
            Column {
                Text(text = employee.name.orEmpty(), style = typography.h6)
                Text(text = employee.username.orEmpty(), style = typography.body1)
                Text(text = employee.email, style = typography.caption)
            }
        }
    }
}
