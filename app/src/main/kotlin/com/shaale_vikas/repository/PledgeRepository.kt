package com.shaale_vikas.repository

import com.google.firebase.database.*
import com.shaale_vikas.model.Pledge
import com.shaale_vikas.utils.Constants
import com.shaale_vikas.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PledgeRepository {

    private val db =
        FirebaseDatabase.getInstance().reference

    private val pledgesRef =
        db.child(Constants.DB_PLEDGES)

    private val needsRef =
        db.child(Constants.DB_NEEDS)

    fun getPledgesFlow(
        needId: String
    ): Flow<Resource<List<Pledge>>> = callbackFlow {

        trySend(Resource.Loading)

        val listener =
            object : ValueEventListener {

                override fun onDataChange(
                    snapshot: DataSnapshot
                ) {

                    val list =
                        snapshot.children
                            .mapNotNull {
                                it.getValue(
                                    Pledge::class.java
                                )
                            }
                            .filter {
                                it.needId == needId
                            }
                            .sortedByDescending {
                                it.timestamp
                            }

                    trySend(Resource.Success(list))
                }

                override fun onCancelled(
                    error: DatabaseError
                ) {

                    trySend(
                        Resource.Error(error.message)
                    )
                }
            }

        pledgesRef.addValueEventListener(listener)

        awaitClose {
            pledgesRef.removeEventListener(listener)
        }
    }

    suspend fun submitPledge(
        pledge: Pledge
    ): Resource<Unit> {

        return try {

            val key =
                pledgesRef.push().key
                    ?: throw Exception("Key error")

            pledgesRef.child(key)
                .setValue(
                    pledge.copy(
                        id = key,
                        timestamp =
                            System.currentTimeMillis()
                    )
                )
                .await()

            val needRef =
                needsRef.child(pledge.needId)

            val snapshot =
                needRef.get().await()

            val current =
                snapshot.child("currentAmount")
                    .getValue(Double::class.java)
                    ?: 0.0

            val target =
                snapshot.child("targetAmount")
                    .getValue(Double::class.java)
                    ?: 0.0

            val newAmount =
                current + pledge.amount

            needRef.updateChildren(
                mapOf(
                    "currentAmount" to newAmount,
                    "isCompleted" to
                            (newAmount >= target)
                )
            ).await()

            Resource.Success(Unit)

        } catch (e: Exception) {

            Resource.Error(
                e.message ?: "Pledge failed"
            )
        }
    }
}