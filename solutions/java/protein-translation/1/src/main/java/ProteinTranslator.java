import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.IntStream;

class ProteinTranslator {
    private static final String STOP = "STOP";
    private static final Map<String, String> CODONS = Map.ofEntries(
            Map.entry("AUG", "Methionine"),
            Map.entry("UUU", "Phenylalanine"),
            Map.entry("UUC", "Phenylalanine"),
            Map.entry("UUA", "Leucine"),
            Map.entry("UUG", "Leucine"),
            Map.entry("UCU", "Serine"),
            Map.entry("UCC", "Serine"),
            Map.entry("UCA", "Serine"),
            Map.entry("UCG", "Serine"),
            Map.entry("UAU", "Tyrosine"),
            Map.entry("UAC", "Tyrosine"),
            Map.entry("UGU", "Cysteine"),
            Map.entry("UGC", "Cysteine"),
            Map.entry("UGG", "Tryptophan"),
            Map.entry("UAA", STOP),
            Map.entry("UAG", STOP),
            Map.entry("UGA", STOP));

    List<String> translate(String rnaSequence) {
        return codons(rnaSequence)
                .map(ProteinTranslator::proteinFor)
                .takeWhile(protein -> !protein.equals(STOP))
                .toList();
    }

    private static Stream<String> codons(String rnaSequence) {
        var codonCount = (rnaSequence.length() + 2) / 3;

        return IntStream.range(0, codonCount)
                .mapToObj(codonNumber -> codonAt(rnaSequence, codonNumber * 3));
    }

    private static String codonAt(String rnaSequence, int index) {
        ensureValidCodon(index + 3 <= rnaSequence.length());

        return rnaSequence.substring(index, index + 3);
    }

    private static String proteinFor(String codon) {
        var protein = CODONS.get(codon);

        ensureValidCodon(protein != null);

        return protein;
    }

    private static void ensureValidCodon(boolean isValid) {
        if (!isValid) {
            throw new IllegalArgumentException("Invalid codon");
        }
    }
}
