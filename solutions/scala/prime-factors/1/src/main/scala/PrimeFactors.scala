import scala.annotation.tailrec

object PrimeFactors:
  def factors(number: Long): List[Long] =
    def isPrimeRemainder(remaining: Long, candidate: Long): Boolean =
      candidate * candidate > remaining

    def divides(remaining: Long, candidate: Long): Boolean =
      remaining % candidate == 0

    @tailrec
    def loop(remaining: Long, candidate: Long, factors: List[Long]): List[Long] =
      if remaining == 1 then factors.reverse
      else if isPrimeRemainder(remaining, candidate) then (remaining :: factors).reverse
      else if divides(remaining, candidate) then loop(remaining / candidate, candidate, candidate :: factors)
      else loop(remaining, candidate + 1, factors)

    loop(number, candidate = 2, factors = Nil)
