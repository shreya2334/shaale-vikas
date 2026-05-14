package com.shaale_vikas.model

data class Pledge(
    val id: String = "",
    val needId: String = "",
    val userId: String = "",
    val userName: String = "",
    val amount: Double = 0.0,
    val message: String = "",
    val timestamp: Long = 0L
)