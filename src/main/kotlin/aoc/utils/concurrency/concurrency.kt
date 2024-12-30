package aoc.utils.concurrency

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

@Suppress("NOTHING_TO_INLINE")
public inline fun <T, R> Iterable<T>.parMap(executor: ExecutorService, noinline transform: (T) -> R): List<R> =
    executor.submitAll(this, transform).map { it.get() }

@Suppress("NOTHING_TO_INLINE")
public inline fun <T, R, C : MutableCollection<in R>> Iterable<T>.parFlatMapTo(executor: ExecutorService, destination: C, noinline transform: (T) -> Iterable<R>): C =
    executor.submitAll(this, transform).flatMapTo(destination) { it.get() }

public inline fun <T, R> ExecutorService.submitAll(inputs: Iterable<T>, crossinline transform: (T) -> R): List<Future<R>> =
    this.invokeAll(inputs.map { Callable { return@Callable transform(it) } })
