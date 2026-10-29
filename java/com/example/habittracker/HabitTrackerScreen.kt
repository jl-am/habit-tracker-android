package com.example.habittracker

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.data.Habit
import com.example.habittracker.data.HabitUiState
import com.example.habittracker.ui.HabitViewModel
import com.example.habittracker.ui.theme.PaleGreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class HabitTrackerScreen(@StringRes val title: Int) {
    HomeScreen(title = R.string.app_name),
    DailyTrackerScreen(title = R.string.daily_tracker),
    AddHabitScreen(title = R.string.add_habit)
}

@Composable
fun HabitTrackerApp(
) {
    val viewModel: HabitViewModel = viewModel()
    val uiState by viewModel.habitUiState.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            HabitBottomBar(navController = navController)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HabitTrackerScreen.HomeScreen.name,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            composable(
                route = HabitTrackerScreen.HomeScreen.name,
            ) {
                HomeScreen(
                    uiState = uiState,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            composable(
                route = HabitTrackerScreen.AddHabitScreen.name,
            ) {
                AddHabitScreen(
                    uiState = uiState,
                    onValueChange = viewModel::onHabitValueChange,
                    onClick = viewModel::addHabit,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            composable(
                route = HabitTrackerScreen.DailyTrackerScreen.name,
            ) {
                DailyTrackerScreen(
                    habitList = uiState.habits,
                    completedHabits = uiState.completedHabits,
                    toggleCompletion = viewModel::toggleCompletion,
                    deleteHabit = viewModel::deleteHabit,
                    selectedHabitId = uiState.selectedHabitId,
                    editInput = uiState.editInput,
                    startEditing = viewModel::startEditing,
                    updateEditInput = viewModel::updateEditInput,
                    confirmEditing = viewModel::confirmEditing,
                    cancelEditing = viewModel::cancelEditing,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    uiState: HabitUiState,
    modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        DisplayCurrentDate()
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
        )
        DailyProgressCard(
            uiState = uiState
        )
        Text(
            text = stringResource(R.string.encouragement),
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(R.drawable.home_imagev2),
            contentDescription = "small steps make big changes",
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun AddHabitScreen(
    uiState: HabitUiState,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.add_a_habit),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.add_question),
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = uiState.habitInput,
                onValueChange = onValueChange,
                label = {
                    Text(
                        text = stringResource(R.string.text_field_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                shape = MaterialTheme.shapes.large,
                isError = uiState.inputError,
                supportingText = {
                    if(uiState.inputError) {
                        Text(
                            text = "Please enter a habit.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

            )
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add a habit",
                    modifier = Modifier
                        .size(52.dp),
                )
            }
        }
    }
}

@Composable
fun DailyTrackerScreen(
    habitList: List<Habit>,
    completedHabits: Set<Int>,
    toggleCompletion: (Int) -> Unit,
    deleteHabit: (Int) -> Unit,
    selectedHabitId: Int?,
    editInput: String,
    startEditing: (Int) -> Unit,
    updateEditInput: (String) -> Unit,
    confirmEditing: () -> Unit,
    cancelEditing: () -> Unit,
    modifier: Modifier = Modifier) {

    LazyColumn(
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 24.dp,
            end = 24.dp,
            bottom = 16.dp
        )
    ) {
        item {
            Text(
                text = stringResource(R.string.daily_tracker),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        items(habitList) { habit ->
            HabitCard(
                habit = habit,
                checked = habit.id in completedHabits,
                onCheckedChange = {
                    toggleCompletion(habit.id)
                },
                onEdit = {
                    startEditing(habit.id)
                },
                onDelete = {
                    deleteHabit(habit.id)
                }
            )
        }
    }

    if(selectedHabitId != null) {
        editPopup(
            habitId = selectedHabitId,
            editInput = editInput,
            onEdit = updateEditInput,
            onConfirm = confirmEditing,
            onDismiss = cancelEditing,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun editPopup(
    habitId: Int,
    editInput: String,
    onEdit: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { // for when the user doesn't use any buttons and maybe just clicks outside the alert dialog
            onDismiss()
        },
        title = {
            Text(
                text = stringResource(R.string.edit_habit),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            OutlinedTextField(
                value = editInput,
                onValueChange =  {newValue ->
                    onEdit(newValue)
                },
                label = {
                    Text(
                        text = "Habit name",
                        style = MaterialTheme.typography.titleMedium)
                },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = {onConfirm()},
                enabled = editInput.isNotEmpty()
            ) {
                Text(
                    text = stringResource(R.string.save_change),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}

@Composable
fun HabitBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationBarItem(
            selected = currentRoute == HabitTrackerScreen.HomeScreen.name,
            onClick = {
                navController.navigate(HabitTrackerScreen.HomeScreen.name) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = stringResource(R.string.home)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.home),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == HabitTrackerScreen.DailyTrackerScreen.name,
            onClick = {
                navController.navigate(HabitTrackerScreen.DailyTrackerScreen.name) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.List,
                  contentDescription = stringResource(R.string.daily_tracker)
              )
            },
            label = {
                Text(
                    text = stringResource(R.string.daily_tracker),
                    style = MaterialTheme.typography.labelMedium
                    )
            }
        )
        NavigationBarItem(
            selected = currentRoute == HabitTrackerScreen.AddHabitScreen.name,
            onClick = {
                navController.navigate(HabitTrackerScreen.AddHabitScreen.name) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_habit)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.add_habit),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        )
    }
}

@Composable
private fun DisplayCurrentDate(modifier: Modifier = Modifier) {
    val currentDate = LocalDate.now()

    val formattedDate = currentDate.format(
        DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.getDefault())
    )

    Text(
        text = formattedDate,
        modifier = modifier,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun DailyProgressCard(
    uiState: HabitUiState,
    modifier: Modifier = Modifier
) {
    val completedCount = uiState.completedCount
    val totalCount = uiState.totalCount
    val percentageComplete = if(totalCount == 0) 0 else (completedCount * 100.0 / totalCount).toInt()

    Card(modifier = modifier
        .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$completedCount/$totalCount",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(R.string.todays_progress),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            LinearProgressIndicator(
                progress = {
                    if(totalCount == 0) {
                        0f
                    } else {
                        completedCount.toFloat() / totalCount
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )
            Text(
                text = "$completedCount out of $totalCount habits completed · $percentageComplete%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun HabitCard(
    habit: Habit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {mutableStateOf(false)} // this is OK because it's local, just for opening and closing the option menu per Habit
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PaleGreen
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
            Text(
                text = habit.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(
                onClick = {expanded = true}
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(stringResource(R.string.edit))
                    },
                    onClick = onEdit
                )
                DropdownMenuItem(
                    text = {
                        Text(stringResource(R.string.delete))
                    },
                    onClick = onDelete
                )
            }
        }
    }
}