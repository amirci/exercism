object Sieve {
  def primes(limit: Int): List[Int] =
    if limit < 2 then List()
    else
      val isPrime = Array.fill(limit + 1)(true)
      isPrime(0) = false
      isPrime(1) = false

      for
        number <- 2 to math.sqrt(limit).toInt
        if isPrime(number)
        multiple <- number * number to limit by number
      do isPrime(multiple) = false

      (2 to limit).filter(isPrime).toList
}
