package com.todo.app.ui.app.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.todo.app.R
import com.todo.app.ui.theme.Primary

@Composable
fun SettingScreen(navigateToLogin: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp, end = 20.dp, start = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 250.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.coming_soon),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(height = 200.dp, width = 200.dp)
                ,contentDescription = "coming_soon"
            )
        }
        Button(
            onClick = navigateToLogin,
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Logout", style = MaterialTheme.typography.labelLarge)
        }

    }
}