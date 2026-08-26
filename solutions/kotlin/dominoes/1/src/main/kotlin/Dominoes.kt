class ChainNotFoundException(msg: String) : RuntimeException(msg)

data class Domino(val left: Int, val right: Int)

object Dominoes {
    fun formChain(vararg inputDominoes: Domino): Chain = formChain(inputDominoes.toList())

    fun formChain(input: Chain): Chain {
        if (input.isEmpty()) {
            return emptyList()
        }

        val firstDomino = input.first()
        val remainingDominoes = input.drop(1)

        return firstDomino
            .orientations()
            .firstSuccessful { domino ->
                findChain(
                    currentRight = domino.right,
                    remaining = remainingDominoes,
                    chainSoFar = listOf(domino),
                )
            }
            ?: throw ChainNotFoundException("No domino chain found.")
    }

    private fun findChain(currentRight: Int, remaining: Chain, chainSoFar: Chain): Chain? {
        if (remaining.isEmpty()) {
            return chainSoFar.takeIf { it.start() == currentRight }
        }

        return remaining
            .candidateMoves(currentRight)
            .firstSuccessful { (candidate, rest) ->
                findChain(candidate.right, rest, chainSoFar + candidate)
            }
    }

    private fun Chain.candidateMoves(currentRight: Int): Sequence<CandidateMove> =
        indices.asSequence().flatMap { index ->
            val rest = withoutIndex(index)
            this[index]
                .orientations()
                .asSequence()
                .filter { it.left == currentRight }
                .map { candidate -> candidate to rest }
        }

    private fun Domino.orientations(): List<Domino> = listOf(this, Domino(right, left))

    private fun Chain.withoutIndex(indexToRemove: Int): Chain = filterIndexed { index, _ -> index != indexToRemove }

    private fun Chain.start(): Int = first().left
}

private typealias Chain = List<Domino>
private typealias CandidateMove = Pair<Domino, Chain>

private fun <T, R : Any> Iterable<T>.firstSuccessful(transform: (T) -> R?): R? = asSequence().firstSuccessful(transform)

private fun <T, R : Any> Sequence<T>.firstSuccessful(transform: (T) -> R?): R? = firstNotNullOfOrNull(transform)
