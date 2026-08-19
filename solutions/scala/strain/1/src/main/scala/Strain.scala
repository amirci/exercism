object Strain {
  def keep[A](input: Seq[A], predicate: A => Boolean): Seq[A] =
    input.foldRight(Seq.empty[A]) { (item, kept) =>
      if (predicate(item)) item +: kept else kept
    }

  def discard[A](input: Seq[A], predicate: A => Boolean): Seq[A] =
    keep(input, item => !predicate(item))
}
