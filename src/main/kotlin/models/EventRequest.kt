package com.jammes.models

import kotlinx.serialization.Serializable

@Serializable
data class EventRequest(
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val location: String,
    val createdBy: Int
)
