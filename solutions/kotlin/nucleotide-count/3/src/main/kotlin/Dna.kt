private const val NUCLEOTIDES = "ACGT"
private val EMPTY_COUNTS = NUCLEOTIDES.associateWith { 0 }

private typealias NucleotideCounts = Map<Char, Int>

class Dna(private val sequence: String) {
    private val actualCounts: NucleotideCounts = sequence.groupingBy { it }.eachCount()

    init {
        require(sequence.all { it in NUCLEOTIDES })
    }

    val nucleotideCounts: NucleotideCounts = EMPTY_COUNTS + actualCounts
}
