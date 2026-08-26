object Sieve {
    fun primesUpTo(upperBound: Int): List<Int> {
        val isComposite = BooleanArray(upperBound + 1)
        val primes = mutableListOf<Int>()

        for (candidate in 2..upperBound) {
            if (!isComposite[candidate]) {
                primes.add(candidate)
                markMultiples(candidate, upperBound, isComposite)
            }
        }

        return primes
    }

    private fun markMultiples(prime: Int, upperBound: Int, isComposite: BooleanArray) {
        for (multiple in prime * prime..upperBound step prime) {
            isComposite[multiple] = true
        }
    }
}
