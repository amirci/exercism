import scala.util.matching.Regex

object RunLengthEncoding {
  private val Run = """(.)\1*""".r
  private val EncodedRun = """(\d+)?(\D)""".r

  def encode(input: String): String =
    Run
      .findAllIn(input)
      .map(encodeRun)
      .mkString

  def decode(input: String): String =
    EncodedRun
      .findAllMatchIn(input)
      .map(expand)
      .mkString

  private def encodeRun(run: String): String =
    if run.length == 1 then run
    else s"${run.length}${run.head}"

  private def expand(encodedRun: Regex.Match): String =
    val count = Option(encodedRun.group(1)).fold(1)(_.toInt)
    encodedRun.group(2) * count
}
