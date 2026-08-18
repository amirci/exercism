object Dominoes {
  type Domino = (Int, Int)
  type Dominoes = List[Domino]
  type Chain = List[Domino]
  type Candidate = (Domino, Dominoes)

  def chain(dominoes: Dominoes): Option[Chain] =
    dominoes match
      case Nil => Some(Nil)
      case first :: rest =>
        List(first, first.swap)
          .distinct
          .flatMap(start => chainFrom(rest, start._2, start._1).map(start :: _))
          .headOption

  private def chainFrom(dominoes: Dominoes, current: Int, target: Int): Option[Chain] =
    dominoes match
      case Nil if current == target => Some(Nil)
      case Nil => None
      case _ =>
        connectingCandidates(dominoes, current)
          .flatMap { case (domino, rest) =>
            chainFrom(rest, domino._2, target).map(domino :: _)
          }
          .headOption

  private def connectingCandidates(dominoes: Dominoes, current: Int): List[Candidate] =
    dominoes.indices.flatMap(index => connectingDominoes(dominoes, index, current)).toList

  private def connectingDominoes(dominoes: List[Domino], index: Int, current: Int): List[Candidate] =
    val domino = dominoes(index)
    val rest = dominoes.patch(index, Nil, 1)

    List(domino, domino.swap).distinct.filter(_._1 == current).map(_ -> rest)
}
