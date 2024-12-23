package aoc.y2024

import aoc.utils.AocDebug
import aoc.utils.Resource
import aoc.utils.containers.BitSetK
import aoc.utils.containers.Tuple3
import aoc.y2024.Day23.Network.Computer
import aoc.y2024.Day23.Network.ComputersSet
import org.apache.commons.collections4.iterators.TransformIterator

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
        var lanParties: Set<ComputersSet> = network.computers.mapTo(HashSet(network.size, 1.0f)) { network.createComputerSet().apply { add(it) } }

        fun ComputersSet.addItIfAllAreConnectedToIt(other: Computer): Int {
            for (computer in this) {
                if (!other.hasConnectionTo(computer)) return 0
            }

            add(other)
            if (AocDebug.enabled) {
                println("added ${other.name} to [${this.sortedBy { it.name }.joinToString(", ") { it.name }}]")
            }

            return 1
        }

        for ((computerA, computerB) in network.uniqueConnections) {
            var grown = 0L

            for (lanParty in lanParties) {
                when {
                    lanParty.contains(computerA) -> grown += lanParty.addItIfAllAreConnectedToIt(computerB)
                    lanParty.contains(computerB) -> grown += lanParty.addItIfAllAreConnectedToIt(computerA)
                }
            }

            if (grown > 5) {
                // set does not drop elements that are internally modified without it getting a ping about it, so we have to help it
                lanParties = lanParties.toSet()
            }
        }

        val largestLanParty = lanParties.maxBy { it.size }

        return largestLanParty.sortedBy { it.name }.joinToString(",") { it.name }
    }

    // Start by looking for sets of three computers where each computer in the set is connected to the other two computers.
    // Consider only sets of three computers where at least one computer's name starts with t.
    fun findAllSetsOfThreeComputersWhereAtLeastOneComputerStartsWithT(): Int {
        val interConnected = HashSet<Tuple3<String>>() // names

        for (midComputer in network.computers) {
            val ok1 = midComputer.name.startsWith("t")

            for (leftComputer in midComputer.connectedComputers) {
                val ok2 = leftComputer.name.startsWith('t')

                for (rightComputer in midComputer.connectedComputers) {
                    if (leftComputer == rightComputer) continue
                    if (!leftComputer.hasConnectionTo(rightComputer)) continue
                    if (!ok1 && !ok2 && !rightComputer.name.startsWith('t')) continue

                    val names = setOf(midComputer.name, leftComputer.name, rightComputer.name).sorted()
                    interConnected.add(Tuple3(names[0], names[1], names[2]))
                }
            }
        }

        if (AocDebug.enabled) {
            interConnected.forEach { println("${it.a},${it.b},${it.c}") }
        }

        return interConnected.size
    }

    fun buildNetwork(): Network {
        val network = Network()

        for ((a, b) in connections) {
            network.addComputer(a)
            network.addComputer(b)
        }

        for ((a, b) in connections) {
            network.addConnection(a, b)
        }

        return network
    }

    class Network(
        val namesToIds: MutableMap<String, Int> = mutableMapOf(),
        val computers: MutableList<Computer> = mutableListOf(),
        val uniqueConnectionIds: MutableList<Pair<Int, Int>> = mutableListOf() // they're not really guaranteed to be unique, but the input seems to be unique enough
    ) {

        val size: Int get() = computers.size

        val uniqueConnections: Sequence<Pair<Computer, Computer>>
            get() = uniqueConnectionIds.asSequence().map { (idA, idB) -> computers[idA] to computers[idB] }

        fun addComputer(name: String): Int =
            namesToIds.computeIfAbsent(name) {
                computers.size.also { computers.add(Computer(it, name)) }
            }

        fun addConnection(a: String, b: String) {
            val idA = namesToIds[a] ?: addComputer(a)
            val idB = namesToIds[b] ?: addComputer(b)

            computers[idA].addConnectionTo(idB)
            computers[idB].addConnectionTo(idA)

            uniqueConnectionIds.add(idA to idB)
        }

        fun createComputerSet(): ComputersSet = ComputersSet()

        inner class Computer(
            val id: Int,
            val name: String,
            var connections: ComputersSet? = null // they're created with default size of computers.size, therefore delaying initialization allows us to initialize with right size
        ) {

            val connectedComputers: Sequence<Computer>
                get() = connections?.asSequence() ?: emptySequence()

            fun addConnectionTo(otherId: Int) {
                val tmp = connections ?: (ComputersSet().also { connections = it })
                tmp.add(otherId)
            }

            fun hasConnectionTo(other: Computer): Boolean =
                connections?.contains(other.id) == true

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is Computer) return false
                return id == other.id
            }

            override fun hashCode(): Int {
                return id.hashCode()
            }

            override fun toString(): String = "$name #$id (${connections?.size})"

        }

        inner class ComputersSet : Iterable<Computer> {

            private var connectionsSet: BitSetK = BitSetK(this@Network.computers.size)

            val size: Int
                get() = connectionsSet.size

            val ids: Sequence<Int>
                get() = connectionsSet.asSequence()

            override fun iterator(): Iterator<Computer> =
                TransformIterator(connectionsSet.iterator()) { this@Network.computers[it] }

            fun add(computer: Computer) {
                add(computer.id)
            }

            fun add(id: Int) {
                connectionsSet.add(id)
            }

            operator fun contains(computer: Computer): Boolean =
                contains(computer.id)

            operator fun contains(id: Int): Boolean =
                connectionsSet[id] == true

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is ComputersSet) return false
                return connectionsSet == other.connectionsSet
            }

            override fun hashCode(): Int =
                connectionsSet.hashCode()

        }

    }

}
