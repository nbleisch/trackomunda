package com.trackomunda.model

import java.time.LocalDate

data class Game(
    val id : String,
    val name: String,
    val gang: Gang? = null,
    val date: LocalDate = LocalDate.now(),
    val round: Int = 1,
)