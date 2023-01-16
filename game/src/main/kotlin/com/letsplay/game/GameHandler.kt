package com.letsplay.game

import reactor.core.publisher.Mono

interface GameHandler<T: Game> {
    val id: String

    val tickRate: Int

    fun create(gameId: String, players: Set<String>, parameters: Map<String, String>): T

    fun onJoin(game: T, player: String): Mono<Boolean>

    fun onLeave(game: T, player: String): Mono<Boolean>

    fun onEnd(game: T): Mono<Void>

    fun onTick(game: T, eventQueue: List<Any>, tick: Long): Mono<Boolean>
}