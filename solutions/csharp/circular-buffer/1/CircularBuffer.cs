public class CircularBuffer<T>
{
    private readonly T[] _buffer;
    private readonly int _capacity;
    private int _readPtr;
    private int _writePtr;

    private int Count => _writePtr - _readPtr;
    private bool IsEmpty => Count == 0;
    private bool IsFull => Count == _capacity;

    public CircularBuffer(int capacity)
    {
        _capacity = capacity;
        _buffer = new T[capacity];
        _writePtr = _readPtr = 0;
    }

    public T Read()
    {
        Require(!IsEmpty);

        return _buffer[_readPtr++ % _capacity];
    }

    public void Write(T value)
    {
        Require(!IsFull);

        _buffer[_writePtr++ % _capacity] = value;
    }

    public void Overwrite(T value)
    {
        if (IsFull)
        {
            DiscardOldestItem();
        }

        _buffer[_writePtr++ % _capacity] = value;
    }

    public void Clear()
    {
        _readPtr = _writePtr;
    }

    private void DiscardOldestItem()
    {
        _readPtr++;
    }

    private void Require(bool condition)
    {
        if (!condition)
        {
            throw new InvalidOperationException();
        }
    }
}
