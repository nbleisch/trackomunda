package com.trackomunda.hexagonal.core.domain.game

enum class GameStatus(val conditions: (Game) -> Boolean) {
    GANG_SELECTION({
        true
    }),
    CREW_SELECTION({ game ->
        GANG_SELECTION.conditions.invoke(game) &&
                game.gang1 != null && game.gang2 != null
    }),
    DEPLOYMENT({ game ->
        CREW_SELECTION.conditions.invoke(game)
    }),
    GAME_STARTED({ game ->
        DEPLOYMENT.conditions.invoke(game)
    })
}
