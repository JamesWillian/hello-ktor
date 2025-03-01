package com.jammes.models

import kotlinx.serialization.Serializable

@Serializable
data class EventDetail(
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val location: String,
    val creatorName: String
)
