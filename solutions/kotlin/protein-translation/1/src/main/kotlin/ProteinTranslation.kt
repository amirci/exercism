private val PROTEINS_BY_CODON =
    mapOf(
        "AUG" to "Methionine",
        "UUU" to "Phenylalanine",
        "UUC" to "Phenylalanine",
        "UUA" to "Leucine",
        "UUG" to "Leucine",
        "UCU" to "Serine",
        "UCC" to "Serine",
        "UCA" to "Serine",
        "UCG" to "Serine",
        "UAU" to "Tyrosine",
        "UAC" to "Tyrosine",
        "UGU" to "Cysteine",
        "UGC" to "Cysteine",
        "UGG" to "Tryptophan",
    )

private val STOP_CODONS = setOf("UAA", "UAG", "UGA")

fun translate(rna: String?): List<String> = rna
    .orEmpty()
    .chunked(CODON_LENGTH)
    .takeWhile { codon -> codon !in STOP_CODONS }
    .map(::proteinFor)

private fun proteinFor(codon: String): String {
    requireThreeLetterCodon(codon)

    return PROTEINS_BY_CODON[codon] ?: throw IllegalArgumentException(INVALID_CODON)
}

private fun requireThreeLetterCodon(codon: String) {
    require(codon.length == CODON_LENGTH) { INVALID_CODON }
}

private const val CODON_LENGTH = 3
private const val INVALID_CODON = "Invalid codon"
