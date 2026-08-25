private val NUCLEOTIDES = listOf('A', 'C', 'G', 'T')

class Dna(private val sequence: String) {
    init {
        require(sequence.all { it in NUCLEOTIDES })
    }

    val nucleotideCounts: Map<Char, Int>
        get() =
            NUCLEOTIDES.associateWith { nucleotide ->
                sequence.count { it == nucleotide }
            }
}
