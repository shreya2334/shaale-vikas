package com.shaale_vikas.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shaale_vikas.model.User
import com.shaale_vikas.utils.Constants
import com.shaale_vikas.utils.Resource
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    private val db =
        FirebaseDatabase.getInstance().reference

    val currentUser
        get() = auth.currentUser

    val isLoggedIn
        get() = auth.currentUser != null

    suspend fun register(
        name: String,
        email: String,
        password: String,
        role: String,
        school: String,
        year: String
    ): Resource<User> {

        return try {

            val result =
                auth.createUserWithEmailAndPassword(
                    email,
                    password
                ).await()

            val uid =
                result.user?.uid
                    ?: throw Exception("UID null")

            val user = User(
                uid,
                name,
                email,
                role,
                school,
                year
            )

            db.child(Constants.DB_USERS)
                .child(uid)
                .setValue(user)
                .await()

            Resource.Success(user)

        } catch (e: Exception) {

            Resource.Error(
                e.message ?: "Registration failed"
            )
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Resource<User> {

        return try {

            auth.signInWithEmailAndPassword(
                email,
                password
            ).await()

            Resource.Success(
                User(
                    uid = auth.currentUser?.uid ?: "",
                    name = "",
                    email = email,
                    role = User.ROLE_ALUMNI,
                    school = "",
                    graduationYear = ""
                )
            )

        } catch (e: Exception) {

            Resource.Error(
                e.message ?: "Login failed"
            )
        }
    }

    suspend fun getUserProfile(
        uid: String
    ): Resource<User> {

        return try {

            val snapshot =
                db.child(Constants.DB_USERS)
                    .child(uid)
                    .get()
                    .await()

            val user =
                snapshot.getValue(User::class.java)
                    ?: throw Exception("Profile not found")

            Resource.Success(user)

        } catch (e: Exception) {

            Resource.Error(
                e.message ?: "Failed to load profile"
            )
        }
    }

    fun logout() {
        auth.signOut()
    }
}