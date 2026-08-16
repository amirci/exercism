object Sieve {
  def primes(limit: Int): List[Int] =
    def sieve(numbers: List[Int]): List[Int] =
      numbers.headOption match
        case None => numbers
        case Some(prime) => prime :: sieve(numbers.tail.filter(_ % prime != 0))

    sieve((2 to limit).toList)
}
