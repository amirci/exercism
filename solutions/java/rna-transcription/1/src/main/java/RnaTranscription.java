class RnaTranscription {

    String transcribe(String dnaStrand) {
        var rna = new StringBuilder(dnaStrand.length());
        for (char nucleotide : dnaStrand.toCharArray()) {
            rna.append(transcribeNucleotide(nucleotide));
        }
        return rna.toString();
    }


    private static char transcribeNucleotide(char nucleotide) {
        return switch (nucleotide) {
            case 'G' -> 'C';
            case 'C' -> 'G';
            case 'T' -> 'A';
            case 'A' -> 'U';
            default -> throw new IllegalArgumentException("Invalid nucleotide: " + nucleotide);
        };
    }
}
