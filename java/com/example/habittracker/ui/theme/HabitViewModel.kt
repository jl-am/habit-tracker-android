package com.example.habittracker.ui

import androidx.lifecycle.ViewModel
import com.example.habittracker.data.Habit
import com.example.habittracker.data.HabitUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HabitViewModel : ViewModel() {
    private val _habitUiState = MutableStateFlow(HabitUiState())
    val habitUiState: StateFlow<HabitUiState> = _habitUiState.asStateFlow()

    var nextHabitId = 1 // track the ids of habit objects

    /** onHabitValueChange()
     * When a user types, update the state to hold the new input.
     * If the text field is empty, it will either display an error or not depending on its previous error state. If it's not empty, it will not display an error.
     */
    fun onHabitValueChange(newValue: String) {
        _habitUiState.update { currentState ->
            currentState.copy(
                habitInput = newValue,
                inputError = if (newValue.isEmpty()) currentState.inputError else false
            )
        }
    }

    /** addHabit
     * Get the input & validate it. If it's invalid, update the state to display an error.
     * If it's valid, create a new Habit object and add it to the habits list. Reset the text field & error state.
     */
    fun addHabit() {
        val name = _habitUiState.value.habitInput.trim()

        if (name.isEmpty()) {
            _habitUiState.update { currentUiState ->
                currentUiState.copy(
                    inputError = true
                )
            }
        } else {
            val newHabit = Habit(
                id = nextHabitId++, // assigns the id & then increments the value for the next object id
                name = name
            )
            _habitUiState.update { currentUiState ->
                currentUiState.copy(
                    habitInput = "",
                    inputError = false,
                    habits = currentUiState.habits + newHabit,
                )
            }
        }
    }

    /** toggleCompletion
     * Add/remove the habit ID to completedHabits accordingly
     */
    fun toggleCompletion(habitId: Int) {
        _habitUiState.update { currentUiState ->
            if (habitId in currentUiState.completedHabits) {
                currentUiState.copy(
                    completedHabits = currentUiState.completedHabits - habitId
                )
            } else {
                currentUiState.copy(
                    completedHabits = currentUiState.completedHabits + habitId
                )
            }
        }
    }

    /** deleteHabit
     *  Remove the habit from habits and completedHabits using the habit's ID
     */
    fun deleteHabit(habitId: Int) {
        _habitUiState.update { currentUiState ->
            currentUiState.copy(
                habits = currentUiState.habits.filter {
                    it.id != habitId
                },
                completedHabits = currentUiState.completedHabits - habitId
            )
        }
    }


    fun startEditing(habitId: Int) {
        _habitUiState.update { currentUiState ->
            val habit = currentUiState.habits.find {it.id == habitId}

            currentUiState.copy(
                selectedHabitId = habitId,
                editInput = habit?.name.orEmpty()
            )
        }
    }

    fun updateEditInput(newValue: String) {
        _habitUiState.update { currentUiState ->
            currentUiState.copy(
                editInput = newValue
            )
        }
    }

    fun cancelEditing() {
        _habitUiState.update { currentUiState ->
            currentUiState.copy(
                selectedHabitId = null,
                editInput = ""
            )
        }
    }

    fun confirmEditing() {
        _habitUiState.update { currentUiState ->
            val habitId = currentUiState.selectedHabitId
            val newValue = currentUiState.editInput.trim()

            if(habitId == null || newValue.isEmpty()) {
                currentUiState
            } else {
                currentUiState.copy(
                    habits = currentUiState.habits.map {habit ->
                        if(habit.id == habitId) {
                            habit.copy(name = newValue)
                        } else {
                            habit
                        }
                    },
                    selectedHabitId = null,
                    editInput = ""
                )
            }
        }
    }
}

//    /** updateHabit
//     * Update the Habit by making a new copy of it.
//     * Find the Habit in the list by checking for the habitId using map and then set the name property to the new value.
//     */
//    fun updateHabit(habitId: Int, newValue: String) {
//        _habitUiState.update {currentUiState ->
//            currentUiState.copy(
//                habits = currentUiState.habits.map { habit ->
//                    if(habit.id == habitId) {
//                        habit.copy(name = newValue)
//                    } else {
//                        habit
//                    }
//                }
//            )
//        }
//    }
//}