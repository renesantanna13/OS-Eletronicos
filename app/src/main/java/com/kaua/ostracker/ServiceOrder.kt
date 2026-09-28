package com.kaua.ostracker

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class DeviceCategory(@StringRes val labelRes: Int, @DrawableRes val iconRes: Int) {
    SMARTPHONE(R.string.category_smartphone, R.drawable.ic_smartphone),
    NOTEBOOK(R.string.category_notebook, R.drawable.ic_laptop),
    VIDEOGAME(R.string.category_videogame, R.drawable.ic_videogame),
    TV(R.string.category_tv, R.drawable.ic_tv)
}

enum class OrderStatus(@StringRes val labelRes: Int, @ColorRes val colorRes: Int) {
    RECEIVED(R.string.status_received, R.color.status_received),
    IN_PROGRESS(R.string.status_in_progress, R.color.status_in_progress),
    READY(R.string.status_ready, R.color.status_ready)
}

data class ServiceOrder(
    val id: String,
    val number: Int,
    val deviceName: String,
    val brand: String,
    val model: String?,
    val category: DeviceCategory,
    val customerName: String,
    val customerPhone: String?,
    val reportedIssue: String,
    val diagnosis: String?,
    val technician: String?,
    val receivedAt: String,
    val estimatedDelivery: String?,
    val estimatedCost: Double?,
    val steps: List<String>,
    val completedSteps: Int = 0,
    val isUrgent: Boolean = false
) {
    init {
        require(steps.isNotEmpty()) { "Service order must have at least one step" }
        require(completedSteps in 0..steps.size) { "Completed steps out of range" }
    }

    val totalSteps: Int
        get() = steps.size

    val progress: Int
        get() = completedSteps * 100 / totalSteps

    // null quando todas as etapas ja foram concluidas
    val currentStep: String?
        get() = steps.getOrNull(completedSteps)

    val status: OrderStatus
        get() = when (completedSteps) {
            0 -> OrderStatus.RECEIVED
            totalSteps -> OrderStatus.READY
            else -> OrderStatus.IN_PROGRESS
        }

    fun advanceStep(): ServiceOrder? =
        if (completedSteps < totalSteps) copy(completedSteps = completedSteps + 1) else null

    fun undoStep(): ServiceOrder? =
        if (completedSteps > 0) copy(completedSteps = completedSteps - 1) else null
}
