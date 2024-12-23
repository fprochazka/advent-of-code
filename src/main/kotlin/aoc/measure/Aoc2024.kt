package aoc.measure

import aoc.utils.Resource
import aoc.y2024.*
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration

fun main() {
    Aoc2024.run()
}

/**
 * This is only meant to give a ballpark - this is obviously NOT how you do benchmarks
 */
object Aoc2024 {

    fun run() {
        println("# 2024")
        setup()
        measureAll()

        println()
        println("# 2024")
        println()
        printResults()
    }

    fun setup() {
        day(1) {
            input { input ->
                task(1) { input.day01().result1 }
                task(2) { input.day01().result2 }
            }
        }

        day(2) {
            input { input ->
                task(1) { input.day02().result1 }
                task(2) { input.day02().result2 }
            }
        }

        day(3) {
            input { input ->
                task(1) { input.day03().result1 }
                task(2) { input.day03().result2 }
            }
        }

        day(4) {
            input { input ->
                task(1) { input.day04().result1 }
                task(2) { input.day04().result2 }
            }
        }

        day(5) {
            input { input ->
                task(1) { input.day05().result1 }
                task(2) { input.day05().result2 }
            }
        }

        day(6) {
            input { input ->
                task(1) { input.day06().result1 }
                task(2) { input.day06().result2 }
            }
        }

        day(7) {
            input { input ->
                task(1) { input.day07().result1 }
                task(2) { input.day07().result2 }
            }
        }

        day(8) {
            input { input ->
                task(1) { input.day08().result1 }
                task(2) { input.day08().result2 }
            }
        }

        day(9) {
            input { input ->
                task(1) { input.day09().result1 }
                task(2) { input.day09().result2 }
            }
            input("perf_test_1", addUpInTotal = false) { input ->
                task(1) { input.day09().result1 }
                task(2) { input.day09().result2 }
            }
            input("perf_test_2", addUpInTotal = false) { input ->
                task(1) { input.day09().result1 }
                task(2) { input.day09().result2 }
            }
        }

        day(10) {
            input { input ->
                task(1) { input.day10().result1 }
                task(2) { input.day10().result2 }
            }
        }

        day(11) {
            input { input ->
                task(1) { input.day11().result1 }
                task(2) { input.day11().result2 }
            }
        }

        day(12) {
            input { input ->
                task(1) { input.day12().result1 }
                task(2) { input.day12().result2 }
            }
        }

        day(13) {
            input { input ->
                task(1) { input.day13().result1 }
                task(2) { input.day13().result2 }
            }
        }

        day(14) {
            input { input ->
                task(1) { input.day14().result1 }
            }
        }

        day(15) {
            input { input ->
                task(1) { input.day15().result1 }
                task(2) { input.day15().result2 }
            }
        }

        day(16) {
            input { input ->
                task(1) { input.day16().result1 }
                task(2) { input.day16().result2 }
            }
        }

        day(17) {
            input { input ->
                task(1) { input.day17().result1 }
                task(2) { input.day17().result2 }
            }
        }

        day(18) {
            input { input ->
                task(1) { input.day18().result1 }
                task(2) { input.day18().result2 }
            }
        }

        day(19) {
            input { input ->
                task(1) { input.day19().result1 }
                task(2) { input.day19().result2 }
            }
        }

        day(20) {
            input { input ->
                task(1) { input.day20().result1 }
                task(2) { input.day20().result2 }
            }
        }

        day(21) {
            input { input ->
                task(1) { input.day21().result1 }
                task(2) { input.day21().result2 }
            }
        }

        day(22) {
            input { input ->
                task(1) { input.day22().result1 }
                task(2) { input.day22().result2 }
            }
        }

        day(23) {
            input { input ->
                task(1) { input.day23().result1 }
                task(2) { input.day23().result2 }
            }
        }
    }

    val resultsByConfig = sortedMapOf<SolverConfig, Runs>(
        Comparator.comparing<SolverConfig, Int> { it.day }
            .thenComparing(Comparator.comparing<SolverConfig, String> { it.taskName })
            .thenComparing(Comparator.comparing<SolverConfig, String> { it.inputName })
    )

    @OptIn(DelicateCoroutinesApi::class)
    fun measureAll() {
        val executor = Executors.newVirtualThreadPerTaskExecutor()!!

        println("Warmup:")
        for (i in 1..20) {
            measureIteration(i, executor)
        }
        resultsByConfig.clear()

        println()
        println("Measure:")
        for (i in 1..10) {
            measureIteration(i, executor)
        }
    }

    private fun measureIteration(i: Int, executor: ExecutorService) {
        print("Iteration ${i.toString().padStart(2, ' ')}: ")

        val iterationDuration = measureTime {
            for (config in runConfigs) {
                val solverRuns = resultsByConfig.getOrPut(config) { Runs() }

                var result: Any? = null
                var taskDuration = Duration.INFINITE

                // this doesn't really kill the solver,
                // but it enables us to continue with the other solvers after the timeout
                val task = executor.submit {
                    taskDuration = measureTime {
                        result = config.solver()
                    }
                }

                try {
                    task.get(10, TimeUnit.SECONDS)

                    solverRuns.record(result, taskDuration)
                    print(".")

                } catch (_: TimeoutException) {
                    task.cancel(true)

                    solverRuns.record("timeout", Duration.INFINITE)
                    print("x")
                }
            }
        }

        print(" ${iterationDuration.toFormattedString().padStart(15)}")
        println()
    }

    fun printResults() {
        var totalRuntime = Duration.ZERO
        var totalBestRuntime = Duration.ZERO

        val taskColumnWidth = resultsByConfig.keys.map { it.taskName }.maxOf { it.length }.coerceAtLeast(4)
        val inputColumnWidth = resultsByConfig.keys.map { it.inputName.toString() }.maxOf { it.length }.coerceAtLeast(5)
        println(" day  ${"task".padStart(taskColumnWidth)} ${"".padStart(inputColumnWidth)}       [average]        [worst]         [best]")

        for ((config, runs) in resultsByConfig) {
            var line = ""
            line += "${config.day.toString().padStart(3)}."
            line += " ${config.taskName.padStart(taskColumnWidth)}."
            line += " ${config.inputName.padStart(inputColumnWidth)}"
            line += ":"
            line += runs.avgTime.toFormattedString().padStart(15)
            line += runs.worstTime.toFormattedString().padStart(15)
            line += runs.bestTime.toFormattedString().padStart(15)
            println(line)

            if (config.addUpInTotal) {
                totalRuntime += runs.avgTime
                totalBestRuntime += runs.bestTime
            }
        }

        println()
        println("Sum of average times for normal inputs: ${totalRuntime.toFormattedString()}")
        println("Sum of best    times for normal inputs: ${totalBestRuntime.toFormattedString()}")

        val second = 1.toDuration(DurationUnit.SECONDS)
        if (totalRuntime > second) {
            println("To get under 1s you have to shave off another ${totalRuntime - second}")
        }
    }

    fun day(day: Int, config: SolverConfigBuilder.() -> Unit) {
        config.invoke(SolverConfigBuilder(day))
    }

    fun Duration.toFormattedString(): String = when {
        this.toInt(DurationUnit.MINUTES) > 0 -> "TIMEOUT"
        else -> toString(DurationUnit.MILLISECONDS, 2)
    }

    val runConfigs = mutableListOf<SolverConfig>()

    data class SolverConfig(
        val day: Int,
        val taskName: String,
        val inputName: String,
        val input: Resource,
        val addUpInTotal: Boolean,
        val solver: () -> Any,
    )

    data class SolverConfigBuilder(
        val day: Int,
        val inputName: String? = null,
        val input: Resource? = null,
        val addUpInTotal: Boolean = true,
    ) {

        fun input(inputName: String = "input", addUpInTotal: Boolean = true, config: SolverConfigBuilder.(Resource) -> Any) {
            val resource = Resource.named("aoc2024/day${day.toString().padStart(2, '0')}/${inputName}.txt");

            config.invoke(
                this.copy(inputName = inputName, addUpInTotal = addUpInTotal, input = resource),
                resource
            )
        }

        fun task(taskName: Int, solver: () -> Any) {
            task("$taskName", solver)
        }

        fun task(taskName: String, solver: () -> Any) {
            runConfigs += SolverConfig(
                day = day,
                taskName = taskName,
                inputName = inputName ?: error("input not set"),
                input = input ?: error("input not set"),
                addUpInTotal = addUpInTotal,
                solver = solver,
            )
        }

    }

    data class Runs(
        val runs: MutableList<Pair<String, Duration>> = mutableListOf(),
    ) {

        val runTimes: List<Duration>
            get() = runs.subList(runs.size / 2, runs.size - 1).map { it.second }

        val avgTime: Duration
            get() = runTimes.map { it.toMicroseconds() }.average().toDuration()

        val worstTime: Duration
            get() = runTimes.maxBy { it.toMicroseconds() }

        val bestTime: Duration
            get() = runTimes.minBy { it.toMicroseconds() }

        fun record(result: Any?, duration: Duration) {
            runs += "$result" to duration
        }

        fun Duration.toMicroseconds(): Long =
            toLong(DurationUnit.MICROSECONDS)

        fun Double.toDuration(): Duration =
            roundToLong().toDuration(DurationUnit.MICROSECONDS)
    }

}
