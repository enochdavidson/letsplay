package com.letsplay.comon.util

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.concurrent.Callable

fun <T> executeBlocking(callable: Callable<T>): Mono<T> {
    return Mono.fromCallable(callable)
        .subscribeOn(Schedulers.boundedElastic())
}