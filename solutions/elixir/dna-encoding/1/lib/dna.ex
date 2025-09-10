defmodule DNA do
  def encode_nucleotide(?A), do: 0b0001
  def encode_nucleotide(?C), do: 0b0010
  def encode_nucleotide(?G), do: 0b0100
  def encode_nucleotide(?T), do: 0b1000
  def encode_nucleotide(?\s), do: 0b0000

  def decode_nucleotide(0b0000), do: ?\s
  def decode_nucleotide(0b0001), do: ?A
  def decode_nucleotide(0b0010), do: ?C
  def decode_nucleotide(0b0100), do: ?G
  def decode_nucleotide(0b1000), do: ?T

  def encode(dna), do: encode("", dna)

  defp encode(result, []), do: result

  defp encode(result, [fst | rst]) do
    encode(<<result::bitstring, encode_nucleotide(fst)::4>>, rst)
  end

  def decode(encoded), do: decode([], encoded)

  defp decode(result, <<>>), do: result

  defp decode(result, <<x::4, rst::bitstring>>) do
    decode(result ++ [decode_nucleotide(x)], rst)
  end
end
