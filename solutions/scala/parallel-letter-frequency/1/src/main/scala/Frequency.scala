import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

object Frequency {
  def frequency(numWorkers: Int, texts: Seq[String]): Map[Char, Int] = {
    if (texts.isEmpty) {
      Map.empty
    } else {
      given ExecutionContext = ExecutionContext.global

      val chunkSize = calculateChunkSize(numWorkers, texts.size)
      val pendingCounts = Future.traverse(texts.grouped(chunkSize).toSeq)(countLetters)

      merge(Await.result(pendingCounts, Duration.Inf))
    }
  }

  private def countLetters(texts: Seq[String])(using ExecutionContext): Future[Map[Char, Int]] =
    Future {
      texts
        .flatMap(_.toLowerCase.filter(_.isLetter))
        .groupMapReduce(identity)(_ => 1)(_ + _)
    }

  private def merge(counts: Seq[Map[Char, Int]]): Map[Char, Int] =
    counts
      .flatten
      .groupMapReduce(_._1)(_._2)(_ + _)

  private def calculateChunkSize(numWorkers: Int, textCount: Int): Int = {
    val workerCount = numWorkers.max(1).min(textCount)
    (textCount + workerCount - 1) / workerCount
  }
}
