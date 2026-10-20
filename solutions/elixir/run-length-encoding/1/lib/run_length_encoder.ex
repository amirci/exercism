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
    |> Enum.map_join(fn [character | _] = run ->
      count = length(run)
      if count == 1, do: character, else: "#{count}#{character}"
    end)
  end

  @spec decode(String.t()) :: String.t()
  def decode(string) do
    Regex.scan(~r/(\d*)(.)/su, string)
    |> Enum.map_join(fn [_, count, character] ->
      repetitions = if count == "", do: 1, else: String.to_integer(count)
      String.duplicate(character, repetitions)
    end)
  end
end
