package com.todo.app.ui.app.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.todo.app.R
import com.todo.app.data.model.TaskUpdateReqModel
import com.todo.app.data.model.Tasks
import com.todo.app.data.repository.TodoRepository
import com.todo.app.helper.classFormatter
import com.todo.app.ui.app.compose.showDatePicker
import com.todo.app.ui.compose.Loading
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    val repository = TodoRepository()
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf<Tasks>() }
    val isLoading = remember { mutableStateOf(true) }
    formattedDate.value = selectedDate.value.format(dateFormatter)

    val getTasks: () -> Unit = {
        coroutineScope.launch {
            repository.getTasks(
                context = localContext,
                date = formattedDate.value,
                onSuccess = {
                    tasks.addAll(it?.tasks.orEmpty())
                    isLoading.value = false
                },
                onFailure = {
                    isLoading.value = false
                    Toast.makeText(localContext, it, Toast.LENGTH_LONG).show()
                },
            )
        }

    }

    LaunchedEffect(formattedDate.value) {
        getTasks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column() {
                Text(
                    text = "Good morning!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Wonder what you will be doing today?",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                )
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        Divider(Modifier.padding(vertical = 20.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "back",
                    modifier = Modifier.clickable(
                        enabled = !isLoading.value
                    ) {
                        selectedDate.value = selectedDate.value.minusDays(1)
                        isLoading.value = true
                        tasks.clear()
//                        getTasks()
                    },
                )
                Text(
                    text = formattedDate.value,
                    modifier = Modifier.clickable {
                        showDatePicker(localContext, selectedDate) {
                            isLoading.value = true
                            tasks.clear()
//                            getTasks()
                        }
                    },
                    style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold,
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_forward_24),
                    contentDescription = "back",
                    modifier = Modifier.clickable(
                        enabled = !isLoading.value
                    ) {
                        selectedDate.value = selectedDate.value.plusDays(1)
                        isLoading.value = true
                        tasks.clear()
//                        getTasks()
                    },
                )
            }
        }
        if (isLoading.value) Loading() else when {
            tasks.isEmpty() -> Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empty_task),
                    contentDescription = "empty"
                )
            }

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Your tasks today are:",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 15.dp),
                    fontWeight = FontWeight.Bold
                )
                LazyColumn {
                    itemsIndexed(tasks) { index, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 5.dp)
                        ) {
                            Checkbox(
                                checked = item.completed!!,
                                onCheckedChange = {
                                    val body = TaskUpdateReqModel(true)
                                    tasks[index] = item.copy(completed = it)
                                    coroutineScope.launch {
                                        repository.updateTask(
                                            context = localContext,
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
                                            id = item.Id!!,
                                            body = body
                                        )
                                    }

                                },
                                modifier = Modifier.size(20.dp),
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.Black,
                                ),
                            )
                            Text(
                                text = item.description!!,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                    }

                }
            }

        }
    }
}