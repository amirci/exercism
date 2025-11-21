package scala

import scala.math.Integral.Implicits.infixIntegralOps


object Say {

  def inEnglish(number: Long): Option[String] = {
    Some(number)
      .filter(isInValidRange)
      .map(toWords)
  }

  private val below20 = Array(
    "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
    "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
  )

  private val exactTens = Array(
    "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
  )

  private def toWords(number: Long): String = {
    if (number == 0) return "zero"

    splitIntoThousands(number)
      .filter(_._1 > 0)
      .map(thousandToWords)
      .reverse
      .reduce((a, b) => s"$a $b")
  }

  private val UNITS = List("", "thousand", "million", "billion")
  
  private def thousandToWords(thousand: (Long, Int)): String = {
    val (nbr, idx)= thousand
    val hundreds = nbr / 100
    val words = List(
      Option.when(hundreds > 0)(s"${below20(hundreds.toInt)} hundred"),
      maybeTensToWords(nbr.toInt),
    ).flatten.mkString(" ")

    val suffix = if(isAtLeastThousands(idx)) s" ${UNITS(idx)}" else ""

    s"$words$suffix"
  }

  private def isAtLeastThousands(idx: Int) = idx > 0

  private def maybeTensToWords(nbr: Int): Option[String] = {
    nbr % 100 match {
      case 0 => None
      case full if full < 20 => Some(below20(full))
      case full =>
        val (tens, units) = full /% 10

        (Option.when(tens > 0)(s"${exactTens(tens - 2)}"), Option.when(units > 0)(s"${below20(units)}")) match {
          case (Some(t), Some(u)) => Some(s"$t-$u")
          case (maybeTens, maybeUnit) => maybeUnit orElse maybeTens
        }
    }
  }

  private def splitIntoThousands(number: Long) = Iterator
    .iterate((number, 0L))((divRem: (Long, Long)) => divRem._1 /% 1000)
    .takeWhile(_ != (0, 0))
    .drop(1)
    .map(_._2)
    .toList
    .zipWithIndex

  private def isInValidRange(number: Long) = {
    0 <= number && number < 1_000_000_000_000L
  }
}