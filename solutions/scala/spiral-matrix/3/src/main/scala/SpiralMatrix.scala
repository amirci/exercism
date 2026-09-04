object SpiralMatrix:
  def spiralMatrix(size: Int): List[List[Int]] =
    if size == 0 then List.empty
    else
      val matrix = Array.fill(size, size)(0)
      var top = 0
      var bottom = size - 1
      var left = 0
      var right = size - 1
      var number = 1

      def fillTopRow(): Unit =
        for column <- left to right do
          matrix(top)(column) = number
          number += 1
        top += 1

      def fillRightColumn(): Unit =
        for row <- top to bottom do
          matrix(row)(right) = number
          number += 1
        right -= 1

      def fillBottomRow(): Unit =
        if top <= bottom then
          for column <- right to left by -1 do
            matrix(bottom)(column) = number
            number += 1
          bottom -= 1

      def fillLeftColumn(): Unit =
        if left <= right then
          for row <- bottom to top by -1 do
            matrix(row)(left) = number
            number += 1
          left += 1

      while top <= bottom && left <= right do
        fillTopRow()
        fillRightColumn()
        fillBottomRow()
        fillLeftColumn()

      matrix.map(_.toList).toList
