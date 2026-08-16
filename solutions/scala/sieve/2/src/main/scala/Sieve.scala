object Sieve {
  def primes(limit: Int): List[Int] =
    if limit < 2 then List()
    else
      val isPrime = Array.fill(limit + 1)(true)
      isPrime(0) = false
      isPrime(1) = false

      var number = 2
      while number * number <= limit do
        if isPrime(number) then
          var multiple = number * number
          while multiple <= limit do
            isPrime(multiple) = false
            multiple += number

        number += 1

      (2 to limit).filter(isPrime).toList
}
