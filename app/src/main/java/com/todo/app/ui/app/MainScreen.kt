package com.todo.app.ui.app

import android.os.Build
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.todo.app.BottomNavItem
import com.todo.app.R
import com.todo.app.ui.app.compose.BottomNavigationBar
import com.todo.app.ui.app.compose.BottomSheetContent
import com.todo.app.ui.app.home.HomeScreen
import com.todo.app.ui.app.setting.SettingScreen
import com.todo.app.ui.theme.Primary
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(navigateToLogin: () -> Unit) {

    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val focusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current
    if (bottomSheetState.currentValue == ModalBottomSheetValue.Hidden) keyboardController?.hide() else keyboardController?.show()

    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent(
                focusRequester,
                closeModal = {
                    navController.navigate(BottomNavItem.Home.screenRoute)
                    coroutineScope.launch {
                        bottomSheetState.hide()
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
            content = {
                NavHost(
                    navController = navController,
                    startDestination = BottomNavItem.Home.screenRoute,
                    modifier = Modifier.padding(it),
                    builder = {
                        composable(BottomNavItem.Home.screenRoute) { HomeScreen() }
                        composable(BottomNavItem.Setting.screenRoute) {
                            SettingScreen(
                                navigateToLogin
                            )
                        }
                    },
                )
            },
        )
    }
}