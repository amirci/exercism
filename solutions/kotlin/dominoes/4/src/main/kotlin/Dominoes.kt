private typealias DominoSide = Int
private typealias Chain = List<Domino>
private typealias DominoBag = List<Domino>

class ChainNotFoundException(msg: String) : RuntimeException(msg)

data class Domino(val left: DominoSide, val right: DominoSide)

object Dominoes {
    fun formChain(vararg inputDominoes: Domino): Chain = formChain(inputDominoes.toList())

    fun formChain(input: DominoBag): Chain {
        if (input.isEmpty()) {
            return emptyList()
        }

        val firstDomino = input.first()
        val remainingDominoes = input.drop(1)
        val chain = mutableListOf(firstDomino)
        val used = BooleanArray(remainingDominoes.size)

        return completeChain(remainingDominoes, used, chain)
            .takeIf { it }
            ?.let { chain.toList() }
            ?: chainNotFound()
    }

    private fun completeChain(remaining: DominoBag, used: BooleanArray, chain: MutableList<Domino>): Boolean {
        if (chain.size == remaining.size + 1) {
            return chain.isClosed()
        }

        for (index in remaining.indices) {
            if (used[index]) {
                continue
            }

            for (candidate in remaining[index].orientations()) {
                if (candidate.left != chain.end()) {
                    continue
                }

                used[index] = true
                chain.add(candidate)

                if (completeChain(remaining, used, chain)) {
                    return true
                }

                chain.removeLast()
                used[index] = false
            }
        }

        return false
    }

    private fun Domino.orientations(): List<Domino> = listOf(this, Domino(right, left))

    private fun Chain.isClosed(): Boolean = start() == end()

    private fun Chain.start(): DominoSide = first().left

    private fun Chain.end(): DominoSide = last().right

    private fun chainNotFound(): Nothing = throw ChainNotFoundException("No domino chain found.")
}
