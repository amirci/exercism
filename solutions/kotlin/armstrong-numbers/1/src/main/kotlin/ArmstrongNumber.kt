object ArmstrongNumber {
    fun check(input: Int): Boolean {
        val digits = input.digits()

        return input == digits.sumOf { it.pow(digits.size) }
    }
}

private fun Int.digits(): List<Int> = generateSequence(this) { it / 10 }
    .takeWhile { it > 0 }
    .map { it % 10 }
    .toList()

private fun Int.pow(exponent: Int): Int = (1..exponent).fold(1) { product, _ -> product * this }
