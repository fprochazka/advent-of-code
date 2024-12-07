package utils

object Combinatorics {

    fun <T> everyCombination(elements: List<T>, size: Int): Sequence<List<T>> = sequence {
        if (size == 0) {
            yield(emptyList())
        } else {
            for (element in elements) {
                for (combination in everyCombination(elements, size - 1)) {
                    yield(listOf(element) + combination)
                }
            }
        }
    }

}
