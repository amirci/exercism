object Hamming {

  def distance(seq1:String, seq2:String): Option[Int] = {
    Option.when(seq1.length == seq2.length) {
        (seq1 zip seq2).count(_ != _)
      }
  }

}