"""Translate RNA codons into proteins."""

PROTEINS_BY_CODON = {
    "AUG": "Methionine",
    "UUU": "Phenylalanine",
    "UUC": "Phenylalanine",
    "UUA": "Leucine",
    "UUG": "Leucine",
    "UCU": "Serine",
    "UCC": "Serine",
    "UCA": "Serine",
    "UCG": "Serine",
    "UAU": "Tyrosine",
    "UAC": "Tyrosine",
    "UGU": "Cysteine",
    "UGC": "Cysteine",
    "UGG": "Tryptophan",
}
STOP_CODONS = {"UAA", "UAG", "UGA"}
CODON_LENGTH = 3


def proteins(strand):
    """Return proteins translated from the RNA strand."""
    translated = []

    for codon in _codons(strand):
        if codon in STOP_CODONS:
            break

        translated.append(PROTEINS_BY_CODON[codon])

    return translated


def _codons(strand):
    return (
        strand[index:index + CODON_LENGTH]
        for index in range(0, len(strand), CODON_LENGTH)
    )
