package com.example.habittracker.data

data class HabitUiState(
    val habitInput: String = "",
    val inputError: Boolean = false,
    val habits: List<Habit> = listOf<Habit>(),
    val completedHabits: Set<Int> = emptySet(),
    val selectedHabitId: Int? = null,
    val editInput: String = "",
) {
    val completedCount: Int
        get() = completedHabits.size

    val totalCount: Int
        get() = habits.size
}
