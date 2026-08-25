object EliudsEggs {
    fun eggCount(number: Int): Int = number
        .toString(radix = 2)
        .count { bit -> bit == '1' }
}
