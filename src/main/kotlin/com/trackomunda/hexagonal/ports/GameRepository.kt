package com.trackomunda.hexagonal.ports

import com.trackomunda.hexagonal.core.Game

interface GameRepository {

    fun createGame(gameName: String): Game

    fun findGame(id: String): Game?

    fun update(game: Game)

    fun findAllGames(): List<Game>

}