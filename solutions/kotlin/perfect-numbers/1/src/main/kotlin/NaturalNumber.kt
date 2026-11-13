
enum class Classification {
    DEFICIENT,
    PERFECT,
    ABUNDANT,
}

fun classify(naturalNumber: Int): Classification {
    require(naturalNumber > 0) { "A natural number must be positive" }

    val sum = aliquotSum(naturalNumber)
    return when {
        sum < naturalNumber -> Classification.DEFICIENT
        sum == naturalNumber.toLong() -> Classification.PERFECT
        else -> Classification.ABUNDANT
    }
}

private fun aliquotSum(number: Int): Long {
    var sum = 0L
    var divisor = 1
    while (divisor.toLong() * divisor <= number) {
        if (number % divisor == 0) {
            val paired = number / divisor
            if (divisor != number) sum += divisor
            if (paired != divisor && paired != number) sum += paired
        }
        divisor++
    }
    return sum
}
