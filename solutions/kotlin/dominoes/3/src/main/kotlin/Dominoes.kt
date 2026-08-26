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

        val graph = Graph.from(input)
        val chain = graph.closedChainStartingAt(input.first().left)

        return chain.takeIf { it.size == input.size && it.isClosed() }
            ?: throw ChainNotFoundException("No domino chain found.")
    }

    private fun Chain.isClosed(): Boolean = start() == end()

    private fun Chain.start(): DominoSide = first().left

    private fun Chain.end(): DominoSide = last().right
}

private class Graph private constructor(
    private val adjacency: Map<DominoSide, MutableList<Edge>>,
    private val edgeCount: Int,
) {
    private val usedEdges = BooleanArray(edgeCount)

    fun closedChainStartingAt(start: DominoSide): Chain {
        val chain = mutableListOf<Domino>()
        visit(start, chain)
        return chain.asReversed()
    }

    private fun visit(side: DominoSide, chain: MutableList<Domino>) {
        val edges = adjacency[side] ?: return

        while (edges.isNotEmpty()) {
            val edge = edges.removeLast()
            if (!usedEdges[edge.id]) {
                usedEdges[edge.id] = true
                val nextSide = edge.otherSide(side)
                visit(nextSide, chain)
                chain.add(Domino(side, nextSide))
            }
        }
    }

    companion object {
        fun from(dominoes: DominoBag): Graph {
            val adjacency = mutableMapOf<DominoSide, MutableList<Edge>>()

            for ((id, domino) in dominoes.withIndex()) {
                val edge = Edge(id, domino.left, domino.right)
                adjacency.getOrPut(domino.left) { mutableListOf() }.add(edge)
                adjacency.getOrPut(domino.right) { mutableListOf() }.add(edge)
            }

            return Graph(adjacency, dominoes.size)
        }
    }
}

private data class Edge(val id: Int, val left: DominoSide, val right: DominoSide) {
    fun otherSide(side: DominoSide): DominoSide = if (side == left) right else left
}
