package com.todo.app.ui.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.todo.app.R
import com.todo.app.data.model.LoginReqModel
import com.todo.app.data.repository.AuthRepository
import com.todo.app.ui.compose.Loading
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    val repository = AuthRepository()

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val localContext = LocalContext.current
    val sharedPref = localContext.getSharedPreferences("my_preference", Context.MODE_PRIVATE);

    val onClicked: () -> Unit = {
        isLoading.value = true
        val user = LoginReqModel(name = username.value, password = password.value)
        coroutineScope.launch {
            repository.signUp(
                user = user,
                onFailure = { message ->
                    Toast.makeText(localContext, message, Toast.LENGTH_LONG).show()
                    println(message)
                    isLoading.value = false
                },
                onSuccess = { result ->
                    println(result);
                    sharedPref.edit().putString("token", result?.token).apply()
                    navController.navigate("MainApp") {
                        popUpTo(0)
                    }
                    isLoading.value = false
                },
            )

        }

        username.value = ""
        password.value = ""

    }


    if (isLoading.value) Loading()
    else

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Icon(
                painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "back",
                modifier = Modifier.clickable {
                    navController.popBackStack()
                },
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text(text = "Username") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (username.value !== "" && password.value !== "") onClicked()
                        },
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onClicked,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = username.value !== "" && password.value !== ""
                ) {
                    Text(text = "Sign up")
                }
            }
        }
}