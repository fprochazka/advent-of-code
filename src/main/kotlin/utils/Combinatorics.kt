package utils

object Combinatorics {

    /**
     * Permutations are based on element positions in the array, the function does not deduplicate equal values.
     *
     * Example: For elements [A] and size 2, it produces sequences [[A, A]].
     * Example: For elements [A, B] and size 2, it produces sequences [[A, A], [A, B], [B, A], [B, B]].
     * Example: For elements [A, B, C] and size 2, it produces sequences [[A, A], [A, B], [A, C], [B, A], [B, B], [B, C], [C, A], [C, B], [C, C]].
     */
    fun <T> permutationsWithRepetition(elements: List<T>, size: Int): Sequence<List<T>> = sequence {
        require(size >= 0) { "Combination size must be non-negative." }

        if (size == 0) {
            yield(emptyList())
            return@sequence
        }

        for (element in elements) {
            for (combination in permutationsWithRepetition(elements, size - 1)) {
                yield(listOf(element) + combination)
            }
        }
    }

    /**
     * Combinations are based on element positions in the array, the function does not deduplicate equal values.
     *
     * For elements [A] and size 2, it produces sequences [].
     * For elements [A, B] and size 2, it produces sequences [[B, A]].
     * For elements [A, B, C] and size 2, it produces sequences [[B, A], [C, A], [C, B]].
     * For elements [A, B, C, D] and size 2, it produces sequences [[B, A], [C, A], [D, A], [C, B], [D, B], [D, C]].
     */
    fun <T> combinationsWithoutRepetition(elements: List<T>, size: Int): Sequence<List<T>> = sequence {
        require(size >= 0) { "Combination size must be non-negative." }

        if (size == 0) {
            yield(emptyList())
            return@sequence
        }

        if (elements.isEmpty()) return@sequence

        val head = elements.first()
        val tail = elements.drop(1)

        // combinations with first element
        for (combination in combinationsWithoutRepetition(tail, size - 1)) {
            yield(combination + head)
        }

        // combinations without the first element
        yieldAll(combinationsWithoutRepetition(tail, size))
    }

}
