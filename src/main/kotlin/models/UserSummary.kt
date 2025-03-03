package com.jammes.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSummary(
    val id: Int,
    val name: String
)