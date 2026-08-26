object Sieve {
    fun primesUpTo(upperBound: Int): List<Int> {
        val isComposite = BooleanArray(upperBound + 1)
        val primes = mutableListOf<Int>()

        fun markMultiples(prime: Int) {
            for (multiple in prime * prime..upperBound step prime) {
                isComposite[multiple] = true
            }
        }

        for (candidate in 2..upperBound) {
            if (!isComposite[candidate]) {
                primes.add(candidate)
                markMultiples(candidate)
            }
        }

        return primes
    }
}
