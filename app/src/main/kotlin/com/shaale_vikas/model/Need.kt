package com.shaale_vikas.model

data class Need(

    val id: String = "",

    val title: String = "",

    val description: String = "",

    val amountNeeded: String = "",

    val pledgedAmount: Int = 0,

    val donors: List<String> = emptyList(),

    val imageUrl: String = "",

    val status: String = "Ongoing",

    val afterImageUrl: String = "",

    val createdBy: String = ""
)