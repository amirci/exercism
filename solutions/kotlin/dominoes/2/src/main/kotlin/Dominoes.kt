private typealias DominoSide = Int

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

        return firstDomino
            .orientations()
            .firstSuccessful { domino -> completeChain(remaining = remainingDominoes, chainSoFar = listOf(domino)) }
            ?: throw ChainNotFoundException("No domino chain found.")
    }

    private fun completeChain(remaining: DominoBag, chainSoFar: Chain): Chain? {
        if (remaining.isEmpty()) {
            return chainSoFar.takeIf { it.isClosed() }
        }

        return remaining
            .candidateMoves(chainSoFar.end())
            .firstSuccessful { (candidate, rest) -> completeChain(rest, chainSoFar + candidate) }
    }

    private fun DominoBag.candidateMoves(currentRight: DominoSide): Sequence<CandidateMove> =
        indices.asSequence().flatMap { index ->
            val rest = withoutIndex(index)
            this[index]
                .orientations()
                .filter { it.left == currentRight }
                .map { candidate -> candidate to rest }
        }

    private fun Domino.orientations(): Sequence<Domino> = sequenceOf(this, Domino(right, left))

    private fun DominoBag.withoutIndex(indexToRemove: Int): DominoBag =
        filterIndexed { index, _ -> index != indexToRemove }

    private fun Chain.isClosed(): Boolean = this.start() == this.end()

    private fun Chain.start(): DominoSide = first().left

    private fun Chain.end(): DominoSide = last().right
}

private typealias Chain = List<Domino>
private typealias DominoBag = List<Domino>
private typealias CandidateMove = Pair<Domino, DominoBag>

private fun <T, R> Sequence<T>.firstSuccessful(transform: (T) -> R?): R? = firstNotNullOfOrNull(transform)
