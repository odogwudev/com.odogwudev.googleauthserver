package com.odogwudev.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class ApiRequest(
    val tokenId: String
)