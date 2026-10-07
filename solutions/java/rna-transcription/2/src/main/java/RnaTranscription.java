import java.util.stream.Collectors;

class RnaTranscription {

    String transcribe(String dnaStrand) {
        return dnaStrand.chars()
                .mapToObj(RnaTranscription::transcribeNucleotide)
                .collect(Collectors.joining());
    }

    private static String transcribeNucleotide(int nucleotide) {
        return switch (nucleotide) {
            case 'G' -> "C";
            case 'C' -> "G";
            case 'T' -> "A";
            case 'A' -> "U";
            default -> throw new IllegalArgumentException("Invalid nucleotide: " + nucleotide);
        };
    }
}
