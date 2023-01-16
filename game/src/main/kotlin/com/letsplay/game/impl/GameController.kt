package com.letsplay.game.impl

import com.letsplay.game.GameCreateRequest
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller

@Controller
class GameController(
    private val gameService: GameService
) {
    @MessageMapping("games/create")
    suspend fun createGame(@AuthenticationPrincipal player: String, request: GameCreateRequest): String {
        return gameService.createGame(player, request)
    }

    @MessageMapping("games/{gameId}/{eventId}")
    suspend fun onGameEvent(
        @AuthenticationPrincipal player: String,
        @DestinationVariable("gameId") gameId: String,
        @DestinationVariable("eventId") eventId: String,
        @Payload data: Any
    ) {
        gameService.addEventToQueue(player, gameId, eventId, data)
    }

    @MessageMapping("games/{gameId}/join")
    suspend fun onJoinRequest(
        @AuthenticationPrincipal player: String,
        @DestinationVariable("gameId") gameId: String,
        @Payload password: String
    ): Boolean {
        return gameService.joinGame(player, gameId, password)
    }
}