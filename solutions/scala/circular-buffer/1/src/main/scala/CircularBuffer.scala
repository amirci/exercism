class EmptyBufferException() extends Exception {}

class FullBufferException() extends Exception {}

class CircularBuffer(capacity: Int) {
  private val values = Array.fill(capacity)(0)
  private var readIndex = 0
  private var writeIndex = 0
  private var count = 0

  def write(value: Int): Unit =
    if isFull then throw FullBufferException()
    else writeUnchecked(value)

  def read(): Int =
    if isEmpty then throw EmptyBufferException()
    else
      val value = values(readIndex)
      readIndex = next(readIndex)
      count -= 1
      value

  def overwrite(value: Int): Unit =
    if isFull then
      values(writeIndex) = value
      writeIndex = next(writeIndex)
      readIndex = writeIndex
    else writeUnchecked(value)

  def clear(): Unit =
    readIndex = 0
    writeIndex = 0
    count = 0

  private def writeUnchecked(value: Int): Unit =
    values(writeIndex) = value
    writeIndex = next(writeIndex)
    count += 1

  private def isFull: Boolean =
    count == capacity

  private def isEmpty: Boolean =
    count == 0

  private def next(index: Int): Int =
    (index + 1) % capacity
}
