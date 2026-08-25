import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.hypot
import kotlin.math.sin

data class ComplexNumber(val real: Double = 0.0, val imag: Double = 0.0) {
    val abs: Double = hypot(real, imag)

    operator fun plus(other: ComplexNumber): ComplexNumber = ComplexNumber(
        real = real + other.real,
        imag = imag + other.imag,
    )

    operator fun minus(other: ComplexNumber): ComplexNumber = ComplexNumber(
        real = real - other.real,
        imag = imag - other.imag,
    )

    operator fun times(other: ComplexNumber): ComplexNumber = ComplexNumber(
        real = real * other.real - imag * other.imag,
        imag = imag * other.real + real * other.imag,
    )

    operator fun div(other: ComplexNumber): ComplexNumber {
        val denominator = other.real * other.real + other.imag * other.imag
        return ComplexNumber(
            real = (real * other.real + imag * other.imag) / denominator,
            imag = (imag * other.real - real * other.imag) / denominator,
        )
    }

    fun conjugate(): ComplexNumber = ComplexNumber(real, -imag)
}

fun exponential(number: ComplexNumber): ComplexNumber {
    val realFactor = exp(number.real)
    return ComplexNumber(
        real = realFactor * cos(number.imag),
        imag = realFactor * sin(number.imag),
    )
}
