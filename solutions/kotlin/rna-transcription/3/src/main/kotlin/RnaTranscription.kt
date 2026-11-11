private val translation = "CGTA".zip("GCAU").toMap()

fun transcribeToRna(dna: String): String =
    dna.map { translation.getValue(it) }.joinToString("")
