defmodule RunLengthEncoder do
  @doc """
  Generates a string where consecutive elements are represented as a data value and count.
  "AABBBCCCC" => "2A3B4C"
  For this example, assume all input are strings, that are all uppercase letters.
  It should also be able to reconstruct the data into its original form.
  "2A3B4C" => "AABBBCCCC"
  """
  @spec encode(String.t()) :: String.t()
  def encode(raw), do: Regex.replace(~r/(.)\1+/su, raw, &encode_run/1)

  @spec decode(String.t()) :: String.t()
  def decode(encoded) do
    Regex.replace(~r/(\d+)(.)/su, encoded, fn _run, count, character ->
      decode_run(count, character)
    end)
  end

  defp encode_run(run), do: "#{String.length(run)}#{String.first(run)}"

  defp decode_run(count, character), do: String.duplicate(character, String.to_integer(count))
end
