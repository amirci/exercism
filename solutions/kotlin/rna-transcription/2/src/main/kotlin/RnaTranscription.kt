private val translation = mapOf('C' to 'G', 'G' to 'C', 'T' to 'A', 'A' to 'U')

fun transcribeToRna(dna: String): String =
    dna.map { translation.getValue(it) }.joinToString("")
