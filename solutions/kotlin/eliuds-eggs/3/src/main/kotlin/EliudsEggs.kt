object EliudsEggs {
    fun eggCount(number: Int): Int =
        generateSequence(number) { it shr 1 }
            .takeWhile { it > 0 }
            .count { (it and 1) == 1 }
}
