package com.jammes.models

import kotlinx.serialization.Serializable

@Serializable
data class EventSummary(
    val id: Int,
    val title: String,
    val date: String,
    val location: String
)
