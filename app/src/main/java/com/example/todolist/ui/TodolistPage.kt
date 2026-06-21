package com.example.todolist.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R
import com.example.todolist.db.ToDo
import com.example.todolist.viewmodel.TodoViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun TodoListPage(todoViewModel: TodoViewModel) {
    val todoList by todoViewModel.todoList.observeAsState(listOf())
    var inputText by remember { mutableStateOf("") }
    var showTitle by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { showTitle = true }

    val fabScale by animateFloatAsState(
        targetValue = if (inputText.isEmpty()) 1f else 1.1f,
        animationSpec = tween(300), label = "fabScale"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = showTitle,
                        enter = fadeIn(animationSpec = tween(800)) + slideInVertically(
                            initialOffsetY = { -40 }, animationSpec = tween(800)
                        ),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    MaterialTheme.colorScheme.primary,
                                                    MaterialTheme.colorScheme.secondary
                                                )
                                            ),
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) { append("To do List") }
                                },
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Text(
                                text = "Your daily tasks",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                windowInsets = TopAppBarDefaults.windowInsets
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        todoViewModel.addTodo(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier.scale(fabScale),
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,

                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Enter todo...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (todoList.isEmpty()) {
                Text(
                    text = "No items yet",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),

                    contentPadding = PaddingValues(top = 4.dp, bottom = 96.dp)
                ) {
                    items(todoList, key = { it.id }) { item ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(600)) +
                                    slideInVertically(initialOffsetY = { 60 }, animationSpec = tween(600)),
                            exit = fadeOut(animationSpec = tween(300)) +
                                    slideOutVertically(targetOffsetY = { 60 }, animationSpec = tween(300))
                        ) {
                            TodoItem(
                                item = item,
                                viewModel = todoViewModel,
                                onDelete = { todoViewModel.deleteTodo(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItem(item: ToDo, viewModel: TodoViewModel, onDelete: () -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var newText by remember { mutableStateOf(item.title) }

    val backgroundColor by animateColorAsState(
        if (item.isDone) MaterialTheme.colorScheme.surfaceVariant
        else MaterialTheme.colorScheme.surface,
        label = "cardColor"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text =SimpleDateFormat("HH:mm, dd/MM", Locale.getDefault()).format(item.createdAt),
                            fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (isEditing) {
                    OutlinedTextField(
                        value = newText,
                        onValueChange = { newText = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                } else {
                    val textColor by animateColorAsState(
                        targetValue = if (item.isDone)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = item.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor,
                        textDecoration = if (item.isDone) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }

            if (isEditing) {
                IconButton(onClick = {
                    if (newText.isNotBlank()) {
                        viewModel.updateTodo(item.copy(title = newText))
                    }
                    isEditing = false
                }) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Save",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                IconButton(onClick = { isEditing = true }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            IconButton(onClick = { viewModel.toggleDone(item) }) {
                Icon(
                    painter = painterResource(id = R.drawable.check_square_svgrepo_com),
                    contentDescription = "check",
                    tint = if (item.isDone)
                        MaterialTheme.colorScheme.outline
                    else
                        MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(id = R.drawable.open_trash_can),
                    contentDescription = "Delete",
                    tint = Color.Black
                )
            }
        }
    }
}
