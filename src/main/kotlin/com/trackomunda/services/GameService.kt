package com.trackomunda.services

import com.trackomunda.model.Game
import java.util.UUID

class GameService {

    private val games: MutableMap<String, Game> = mutableMapOf()

    fun createGame(): Game {
        val newGame = Game(id = UUID.randomUUID().toString())
        games[newGame.id] = newGame
        return newGame
    }

    fun findGame(id: String) = games[id]?.copy()

    fun update(game: Game) {
        games[game.id] = game
    }

    fun getGames(): List<Game> {
        return games.values.toList()
    }

}