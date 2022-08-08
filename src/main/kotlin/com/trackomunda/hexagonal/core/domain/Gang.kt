package com.trackomunda.hexagonal.core.domain

data class Gang(
    val name: String,
    val ganger: List<Ganger>
)