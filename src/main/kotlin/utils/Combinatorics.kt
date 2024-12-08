package utils

object Combinatorics {

    /**
     * Permutations are based on element positions in the list, the function does not deduplicate equal values.
     *
     * Returns all orderings, i.e. it will contain both [A, B] and [B, A]
     *
     * Example: For elements [A], it produces sequences [[A]].
     * Example: For elements [A, B], it produces sequences [[A, A], [A, B], [B, A], [B, B]].
     * Example: For elements [A, B, C], it produces sequences [[A, A, A], [A, A, B], [A, A, C], [A, B, A], [A, B, B], [A, B, C], [A, C, A], [A, C, B], [A, C, C], [B, A, A], [B, A, B], [B, A, C], [B, B, A], [B, B, B], [B, B, C], [B, C, A], [B, C, B], [B, C, C], [C, A, A], [C, A, B], [C, A, C], [C, B, A], [C, B, B], [C, B, C], [C, C, A], [C, C, B], [C, C, C]].
     */
    fun <T> permutationsWithRepetition(elements: List<T>): Sequence<List<T>> = variationsWithRepetition(elements, elements.size)

    /**
     * Permutations are based on element positions in the list, the function does not deduplicate equal values.
     *
     * Returns all orderings, i.e. it will contain both [A, B] and [B, A]
     *
     * For elements [A], it produces sequences [[A]].
     * For elements [A, B], it produces sequences [[A, B], [B, A]].
     * For elements [A, B, C], it produces sequences [[A, B, C], [A, C, B], [B, A, C], [B, C, A], [C, A, B], [C, B, A]].
     */
    fun <T> permutationsWithoutRepetition(elements: List<T>): Sequence<List<T>> = variationsWithoutRepetition(elements, elements.size)

    /**
     * Combinations are based on element positions in the list, the function does not deduplicate equal values.
     *
     * Does not return all orderings, i.e. when it already returned [A, B] it will not return [B, A]
     *
     * For elements [A] and size 2, it produces sequences [A, A].
     * For elements [A, B] and size 2, it produces sequences [[A, A], [A, B], [B, B]].
     * For elements [A, B, C] and size 2, it produces sequences [[A, A], [A, B], [A, C], [B, B], [B, C], [C, C]].
     * For elements [A, B, C, D] and size 2, it produces sequences [[A, A], [A, B], [A, C], [A, D], [B, B], [B, C], [B, D], [C, C], [C, D], [D, D]].
     */
    fun <T> combinationsWithRepetition(elements: List<T>, size: Int): Sequence<List<T>> = sequence {
        require(size >= 0) { "Combination size must be non-negative." }

        if (size == 0) {
            yield(emptyList())
            return@sequence
        }

        for (index in elements.indices) {
            val element = elements[index]
            val tail = elements.subList(index, elements.size)

            for (combination in combinationsWithRepetition(tail, size - 1)) {
                yield(listOf(element) + combination)
            }
        }
    }

    /**
     * Combinations are based on element positions in the list, the function does not deduplicate equal values.
     *
     * Does not return all orderings, i.e. when it already returned [A, B] it will not return [B, A]
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

    /**
     * Variations are based on element positions in the list, the function does not deduplicate equal values.
     *
     * Returns all orderings, i.e. it will contain both [A, B] and [B, A]
     *
     * For elements [A] and size 2, it produces sequences [[A, A]].
     * For elements [A, B] and size 2, it produces sequences [[A, A], [A, B], [B, A], [B, B]].
     * For elements [A, B, C] and size 2, it produces sequences [[A, A], [A, B], [A, C], [B, A], [B, B], [B, C], [C, A], [C, B], [C, C]].
     * For elements [A, B, C, D] and size 2, it produces sequences [[A, A], [A, B], [A, C], [A, D], [B, A], [B, B], [B, C], [B, D], [C, A], [C, B], [C, C], [C, D], [D, A], [D, B], [D, C], [D, D]].
     */
    fun <T> variationsWithRepetition(elements: List<T>, size: Int): Sequence<List<T>> = sequence {
        require(size >= 0) { "Variation size must be non-negative." }

        if (size == 0) {
            yield(emptyList())
            return@sequence
        }

        for (element in elements) {
            for (variation in variationsWithRepetition(elements, size - 1)) {
                yield(listOf(element) + variation)
            }
        }
    }

    /**
     * Variations are based on element positions in the list, the function does not deduplicate equal values.
     *
     * Returns all orderings, i.e. it will contain both [A, B] and [B, A]
     *
     * For elements [A] and size 2, it produces sequences [].
     * For elements [A, B] and size 2, it produces sequences [[A, B], [B, A]].
     * For elements [A, B, C] and size 2, it produces sequences [[A, B], [A, C], [B, A], [B, C], [C, A], [C, B]].
     * For elements [A, B, C, D] and size 2, it produces sequences [[A, B], [A, C], [A, D], [B, A], [B, C], [B, D], [C, A], [C, B], [C, D], [D, A], [D, B], [D, C]].
     */
    fun <T> variationsWithoutRepetition(elements: List<T>, size: Int): Sequence<List<T>> = sequence {
        require(size >= 0) { "Combination size must be non-negative." }

        if (size == 0) {
            yield(emptyList())
            return@sequence
        }

        for ((index, element) in elements.withIndex()) {
            val remaining = elements.filterIndexed { i, _ -> i != index }

            for (permutation in variationsWithoutRepetition(remaining, size - 1)) {
                yield(listOf(element) + permutation)
            }
        }
    }

}
