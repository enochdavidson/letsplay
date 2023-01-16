package com.letsplay.game.impl

import com.google.common.cache.CacheBuilder
import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimaps
import com.letsplay.game.Game
import com.letsplay.game.GameCreateRequest
import com.letsplay.game.GameEvent
import com.letsplay.game.GameHandler
import com.letsplay.realtime.Realtime
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.Disposable
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class GameService(
    private val realtime: Realtime,
    gameHandlerList: List<GameHandler<Game>>
) {
    private val logger = LoggerFactory.getLogger(GameService::class.java)

    private val gameHandlers = mutableMapOf<String, GameHandler<Game>>().apply {
        gameHandlerList.forEach { put(it.id, it) }
    }

    private val games = CacheBuilder.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(60))
        .initialCapacity(100)
        .removalListener<String, Game> { onGameExpired(it.key!!) }
        .build<String, Game>()
        .asMap()

    private val eventQueue = Multimaps.synchronizedListMultimap(LinkedListMultimap.create<String, Any>())

    private val gameLoops = ConcurrentHashMap<String, Disposable>()

    fun createGame(player: String, request: GameCreateRequest): String {
        val handler = gameHandlers[request.name]
        if (handler != null) {
            val gameId = UUID.randomUUID().toString()
            val game = handler.create(gameId, setOf(player), request.params)
            games[gameId] = game
            startLoop(handler, gameId, game)
            logger.debug("Game created. Id: {}", gameId)
            return gameId
        } else {
            throw IllegalArgumentException("Game with name '${request.name}' is not registered")
        }
    }

    private fun startLoop(handler: GameHandler<Game>, gameId: String, game: Game) {
        val loop = Flux.interval(Duration.ofMillis(1000L / handler.tickRate))
            .flatMap { handler.onTick(game, eventQueue.removeAll(gameId), it) }
            .doOnNext { continueLoop ->
                if (!continueLoop) {
                    endLoop(gameId)
                }
            }
            .onErrorContinue { t, _ -> logger.error("Error occurred in game loop", t) }
            .doFinally { handler.onEnd(game) }
            .subscribe()
        gameLoops[gameId] = loop
    }

    private fun endLoop(gameId: String) {
        gameLoops.remove(gameId)?.dispose()
    }

    private fun onGameExpired(gameId: String) {
        gameLoops.remove(gameId)?.dispose()
        eventQueue.removeAll(gameId)
    }

    fun addEventToQueue(
        player: String,
        gameId: String,
        eventId: String,
        data: Any
    ) {
        logger.debug("player: {}, game: {}, event: {}, data: {}", player, gameId, eventId, data)
        eventQueue.put(gameId, GameEvent(eventId, data))
    }

    suspend fun joinGame(player: String, gameId: String, password: String): Boolean {
        val game = games[gameId]
        if (game != null) {
            return gameHandlers[game.name]!!.onJoin(game, player).awaitSingle()
        }
        return false
    }
}