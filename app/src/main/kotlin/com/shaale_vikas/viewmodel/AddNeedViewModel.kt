package com.shaale_vikas.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.shaale_vikas.model.Need

class AddNeedViewModel : ViewModel() {

    fun addNeed(

        title: String,

        description: String,

        amount: String,

        imageUrl: String,

        createdBy: String,

        onSuccess: () -> Unit,

        onError: (String) -> Unit
    ) {

        val database =

            FirebaseDatabase
                .getInstance()
                .reference

        val needId =
            database
                .child("needs")
                .push()
                .key ?: return

        val need = Need(

            id = needId,

            title = title,

            description = description,

            amountNeeded = amount,

            imageUrl = imageUrl,

            pledgedAmount = 0,

            donors = emptyList(),

            createdBy = createdBy
        )

        database
            .child("needs")
            .child(needId)
            .setValue(need)

            .addOnSuccessListener {

                onSuccess()
            }

            .addOnFailureListener {

                onError(
                    it.message ?: "Failed"
                )
            }
    }
}