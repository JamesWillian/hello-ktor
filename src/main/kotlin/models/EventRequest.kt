package com.jammes.models

import kotlinx.serialization.Serializable

@Serializable
data class EventRequest(
    val title: String? = null,
    val description: String? = null,
    val date: String? = null,
    val time: String? = null,
    val location: String? = null,
    val createdBy: Int? = null
)
