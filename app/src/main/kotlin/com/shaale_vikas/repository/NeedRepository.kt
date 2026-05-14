package com.shaale_vikas.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shaale_vikas.model.Need

class NeedRepository {

    private val database =

        FirebaseDatabase
            .getInstance()
            .reference
            .child("needs")

    fun observeNeeds(
        onDataChanged:
            (List<Need>) -> Unit
    ) {

        database.addValueEventListener(

            object : ValueEventListener {

                override fun onDataChange(
                    snapshot: DataSnapshot
                ) {

                    val needsList =
                        mutableListOf<Need>()

                    for (child in
                    snapshot.children
                    ) {

                        val need =
                            child.getValue(
                                Need::class.java
                            )

                        if (need != null) {

                            needsList.add(need)
                        }
                    }

                    onDataChanged(needsList)
                }

                override fun onCancelled(
                    error: DatabaseError
                ) {

                }
            }
        )
    }
    fun updateNeed(
        need: Need
    ) {

        database
            .child(need.id)
            .setValue(need)
    }
}