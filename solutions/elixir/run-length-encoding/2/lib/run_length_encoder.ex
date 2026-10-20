defmodule RunLengthEncoder do
  @doc """
  Generates a string where consecutive elements are represented as a data value and count.
  "AABBBCCCC" => "2A3B4C"
  For this example, assume all input are strings, that are all uppercase letters.
  It should also be able to reconstruct the data into its original form.
  "2A3B4C" => "AABBBCCCC"
  """
  @spec encode(String.t()) :: String.t()
  def encode(string) do
    string
    |> String.graphemes()
    |> Enum.chunk_by(& &1)
    |> Enum.map_join(&encode_run/1)
  end

  @spec decode(String.t()) :: String.t()
  def decode(encoded) do
    Regex.scan(~r/(\d*)(.)/su, encoded)
    |> Enum.map_join(&decode_run/1)
  end

  defp encode_run([character | _] = run) do
    case length(run) do
      1 -> character
      count -> "#{count}#{character}"
    end
  end

  defp decode_run([_, count, character]) do
    repetitions = if count == "", do: 1, else: String.to_integer(count)
    String.duplicate(character, repetitions)
  end
end
