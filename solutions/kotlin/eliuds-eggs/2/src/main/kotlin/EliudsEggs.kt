object EliudsEggs {
    fun eggCount(number: Int): Int = number
        .toString(radix = 2)
        .count { it == '1' }
}
