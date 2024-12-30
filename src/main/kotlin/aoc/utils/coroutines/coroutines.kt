package aoc.utils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalCoroutinesApi::class)
public fun <T> runBlocking(parallelism: Int, block: suspend CoroutineScope.() -> T): T {
    return runBlocking(Dispatchers.Default.limitedParallelism(parallelism), block)
}
