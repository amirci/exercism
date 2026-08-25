private typealias Score = Int
private typealias Letter = Char

object ETL {
    fun transform(source: Map<Score, Collection<Letter>>): Map<Letter, Score> = source
        .flatMap { (score, letters) -> letters.map { letter -> letter.lowercaseChar() to score } }
        .toMap()
}
