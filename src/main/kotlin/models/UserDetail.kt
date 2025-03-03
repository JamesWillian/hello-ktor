package com.jammes.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDetail(
    val id: Int,
    val name: String,
    val email: String,
    val bornAt: String,
    val password: String? = null
)