package com.todo.app.ui.app

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.todo.app.BottomNavItem
import com.todo.app.R
import com.todo.app.data.model.Tasks
import com.todo.app.data.repository.TodoRepository
import com.todo.app.ui.app.compose.BottomNavigationBar
import com.todo.app.ui.app.compose.BottomSheetContent
import com.todo.app.ui.app.home.HomeScreen
import com.todo.app.ui.app.setting.SettingScreen
import com.todo.app.ui.theme.Primary
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(navigateToLogin: () -> Unit) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val repository = TodoRepository()
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val formattedDate = remember { mutableStateOf("") }
    formattedDate.value = selectedDate.value.format(dateFormatter)
    val isHomeLoading = remember { mutableStateOf(true) }

    val focusRequester = remember { FocusRequester() }
    val tasks = remember { mutableStateListOf<Tasks>() }

    val localContext = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun getTasks(date: String): Unit {
        isHomeLoading.value = true
        coroutineScope.launch {
            repository.getTasks(
                context = localContext,
                date = date,
                onSuccess = { data ->
                    tasks.addAll(data?.tasks.orEmpty())
                },
                onFailure = {
                    Toast.makeText(localContext, it, Toast.LENGTH_SHORT).show()
                },
            )
            isHomeLoading.value = false;
        }
    }

    LaunchedEffect(bottomSheetState) {
        snapshotFlow { bottomSheetState.isVisible }.collect {
            if (it) keyboardController?.show()
            else keyboardController?.hide()
        }
    }

    LaunchedEffect(selectedDate.value) {
        println("Get tasks");
        coroutineScope.launch {
            getTasks(formattedDate.value)
        }
    }


    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent(
                focusRequester,
                selectedDate = selectedDate,
                closeModal = {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                        tasks.clear()
                        getTasks(formattedDate.value);
                    }
                },
            )
        },
        sheetShape = RoundedCornerShape(
            topStart = 20.dp, topEnd = 20.dp,
        ),
        sheetState = bottomSheetState,
    ) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                    cutoutShape = CircleShape,
                    backgroundColor = Primary
                ) {
                    BottomNavigationBar(navController)
                }

            },
            modifier = Modifier.fillMaxSize(),
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            focusRequester.requestFocus()
                            bottomSheetState.show()
                        }
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        tint = Color.White,
                        contentDescription = "add"
                    )
                }
            },
        ) { it ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.screenRoute,
                modifier = Modifier.padding(it),
            ) {
                composable(BottomNavItem.Home.screenRoute) {
                    HomeScreen(
                        tasks,
                        onForwardDate = {
                            selectedDate.value = selectedDate.value.plusDays(1)
                        },
                        onMinusDate = {
                            selectedDate.value = selectedDate.value.minusDays(1)
                        },
                        date = selectedDate,
                        formattedDate = formattedDate.value,
                        isLoading = isHomeLoading,
                        updateTasks = { id, body ->
                            coroutineScope.launch {
                                repository.updateTask(context = localContext,
                                    onSuccess = { result ->
                                        Toast.makeText(
                                            localContext,
                                            result?.message,
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    },
                                    onFailure = { message ->
                                        Toast.makeText(
                                            localContext, message, Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    id = id,
                                    body = body
                                )
                            }
                        },
                    )
                }
                composable(BottomNavItem.Setting.screenRoute) {
                    SettingScreen(navigateToLogin = {
                        tasks.clear()
                        navigateToLogin()
                    })
                }
            }
        }
    }
}