"""Translate RNA codons into proteins."""

from itertools import takewhile

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
    return [PROTEINS_BY_CODON[codon] for codon in _codons_until_stop(strand)]


def _codons_until_stop(strand):
    return takewhile(lambda codon: codon not in STOP_CODONS, _split_into_codons(strand))


def _split_into_codons(strand):
    return (
        strand[index:index + CODON_LENGTH]
        for index in range(0, len(strand), CODON_LENGTH)
    )
