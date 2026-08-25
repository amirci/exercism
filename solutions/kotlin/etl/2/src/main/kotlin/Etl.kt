private typealias Score = Int
private typealias Letter = Char

object ETL {
    fun transform(source: Map<Score, Collection<Letter>>): Map<Letter, Score> = source
        .flatMap(::letterScores)
        .toMap()

    private fun letterScores(entry: Map.Entry<Score, Collection<Letter>>): List<Pair<Letter, Score>> =
        entry.value.map { letter -> letter.lowercaseChar() to entry.key }
}
