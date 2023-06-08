package com.todo.app

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.todo.app.data.repository.TodoRepository
import com.todo.app.ui.app.MainScreen
import com.todo.app.ui.auth.LoginScreen
import com.todo.app.ui.auth.SignUpScreen
import com.todo.app.ui.compose.Loading
import kotlinx.coroutines.launch

sealed class AppScreen(val screenRoute: String) {
    object Login : AppScreen("Login")
    object Signup : AppScreen("Signup")
}


sealed class BottomNavItem(val title: String, var icon: Int, var screenRoute: String) {
    object Home : BottomNavItem("Home", R.drawable.baseline_home_24, "home")
    object Setting : BottomNavItem("Setting", R.drawable.baseline_settings_24, "setting")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoApp() {
    val navController = rememberNavController()
    val localContext = LocalContext.current
    val sharedPref = localContext.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
    val token = sharedPref.getString("token", "")
    val mainDestination: String = if (token === "") "Login" else "MainApp"
    val coroutineScope = rememberCoroutineScope()
    val repository = TodoRepository()
    val isLoading = remember { mutableStateOf(false) }

    if (isLoading.value) Loading()
    else NavHost(navController = navController, startDestination = mainDestination) {
        composable(AppScreen.Login.screenRoute) { LoginScreen(navController) }
        composable(AppScreen.Signup.screenRoute) { SignUpScreen(navController) }
        composable("MainApp") {
            MainScreen {
                isLoading.value = true
                coroutineScope.launch {
                    repository.logout(
                        context = localContext,
                        onSuccess = {
                            isLoading.value = false
                            sharedPref.edit().remove("token").apply()
                            navController.navigate(AppScreen.Login.screenRoute)
                        },
                        onFailure = {
                            isLoading.value = false
                            println(it)
                            if (it !== null) Toast.makeText(localContext, it, Toast.LENGTH_LONG)
                                .show()
                        },
                    )
                }
            }
        }
    }
}