package com.shaale_vikas.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = ROLE_ALUMNI,
    val school: String = "",
    val graduationYear: String = ""
) {

    fun isAdmin() = role == ROLE_ADMIN

    companion object {
        const val ROLE_ADMIN = "admin"
        const val ROLE_ALUMNI = "alumni"
    }
}