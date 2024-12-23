package aoc.y2024

import aoc.utils.AocDebug
import aoc.utils.Resource
import aoc.utils.containers.Tuple3

fun Resource.day23(): Day23 = Day23(
    nonBlankLines().map { it.split("-", limit = 2).let { it -> it[0] to it[1] } }
)

data class Day23(val connections: List<Pair<String, String>>) {

    val network by lazy { buildNetwork() }

    val result1 by lazy { findAllSetsOfThreeComputersWhereAtLeastOneComputerStartsWithT() }
    val result2 by lazy { passwordForTheLargestSetOfComputersThatAreAllConnectedToEachOther() }

    // The LAN party will be the largest set of computers that are all connected to each other.
    // That is, for each computer at the LAN party, that computer will have a connection to every other computer at the LAN party.
    // The LAN party posters say that the password to get into the LAN party is the name of every computer at the LAN party, sorted alphabetically, then joined together with commas.
    fun passwordForTheLargestSetOfComputersThatAreAllConnectedToEachOther(): String {
        var lanParties: MutableSet<MutableSet<Computer>> = network.map { mutableSetOf(it.value) }.toMutableSet()

        fun MutableSet<Computer>.allAreConnectedTo(other: Computer): Boolean {
            for (computer in this) {
                if (computer !in other.connections) return false
            }
            return true
        }

        for ((i, connection) in connections.withIndex()) {
            val computerA = network[connection.first]!!
            val computerB = network[connection.second]!!

            var grown = 0L

            for (lanParty in lanParties) {
                when {
                    lanParty.contains(computerA) -> {
                        if (lanParty.allAreConnectedTo(computerB)) {
                            lanParty.add(computerB)
                            grown += 1
                            if (AocDebug.enabled) {
                                println("added ${computerB.name} to [${lanParty.sortedBy { it.name }.joinToString(", ") { it.name }}]")
                            }
                        }
                    }

                    lanParty.contains(computerB) -> {
                        if (lanParty.allAreConnectedTo(computerA)) {
                            lanParty.add(computerA)
                            grown += 1
                            if (AocDebug.enabled) {
                                println("added ${computerA.name} to [${lanParty.sortedBy { it.name }.joinToString(", ") { it.name }}]")
                            }
                        }
                    }
                }
            }

            if (grown > 5) {
                val beforeDistinct = lanParties.size
                lanParties = lanParties.distinct().toMutableSet()
                if (AocDebug.enabled) {
                    if (lanParties.size < beforeDistinct) {
                        println("distinct: $beforeDistinct -> ${lanParties.size}")
                    } else {
                        println("useless distinct")
                    }
                }
            }
        }

        val largestLanParty = lanParties.maxBy { it.size }

        return largestLanParty.sortedBy { it.name }.joinToString(",") { it.name }
    }

    // Start by looking for sets of three computers where each computer in the set is connected to the other two computers.
    // Consider only sets of three computers where at least one computer's name starts with t.
    fun findAllSetsOfThreeComputersWhereAtLeastOneComputerStartsWithT(): Int {
        val interConnected = HashSet<Tuple3<String>>() // names

        for ((name, computer) in network) {
            if (computer.connections.size <= 1) continue
            val ok1 = computer.name.startsWith("t")

            for (leftComputer in computer.connections) {
                val ok2 = leftComputer.name.startsWith('t')

                for (rightComputer in computer.connections) {
                    if (leftComputer == rightComputer) continue
                    if (rightComputer !in leftComputer.connections) continue
                    if (!ok1 && !ok2 && !rightComputer.name.startsWith('t')) continue

                    val names = setOf(name, leftComputer.name, rightComputer.name).sorted()
                    interConnected.add(Tuple3(names[0], names[1], names[2]))
                }
            }
        }

        if (AocDebug.enabled) {
            interConnected.forEach { println("${it.a},${it.b},${it.c}") }
        }

        return interConnected.size
    }

    fun buildNetwork(): Map<String, Computer> {
        val network = mutableMapOf<String, Computer>()

        for ((a, b) in connections) {
            network.put(a, Computer(a))
            network.put(b, Computer(b))
        }

        for ((a, b) in connections) {
            val computerA = network[a]!!
            val computerB = network[b]!!

            computerA.connections.add(computerB)
            computerB.connections.add(computerA)
        }

        return network
    }

    data class Computer(val name: String, val connections: MutableSet<Computer> = mutableSetOf()) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Computer) return false
            return name == other.name
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }

        override fun toString(): String = "$name (connections=${connections.size})"

    }

}
