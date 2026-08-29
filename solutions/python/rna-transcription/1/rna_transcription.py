"""RNA transcription implementation."""

TRANSCRIPTION = str.maketrans("GCTA", "CGAU")


def to_rna(dna_strand):
    """Return the RNA complement for a DNA strand."""
    return dna_strand.translate(TRANSCRIPTION)
