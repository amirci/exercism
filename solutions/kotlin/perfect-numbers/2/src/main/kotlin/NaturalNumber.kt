
import kotlin.math.sqrt

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

private fun aliquotSum(number: Int): Long =
    divisorsUpToSquareRoot(number)
        .sumOf { divisor -> properDivisorPairSum(number, divisor) }

private fun properDivisorPairSum(number: Int, divisor: Int): Long {
    val pairedDivisor = number / divisor
    val divisorSum = if (divisor < number) divisor.toLong() else 0L
    val pairedSum =
        if (pairedDivisor != divisor && pairedDivisor < number) pairedDivisor.toLong()
        else 0L

    return divisorSum + pairedSum
}

private fun divisorsUpToSquareRoot(number: Int) =
    generateSequence(1) { it + 1 }
        .takeWhile { it <= sqrt(number.toDouble()) }
        .filter { isDivisor(number, it) }

private fun isDivisor(number: Int, candidate: Int): Boolean = number % candidate == 0
