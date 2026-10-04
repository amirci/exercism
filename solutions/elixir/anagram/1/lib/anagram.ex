defmodule Anagram do
  @doc """
  Returns all candidates that are anagrams of, but not equal to, 'base'.
  """
  @spec match(String.t(), [String.t()]) :: [String.t()]
  def match(base, candidates) do
    normalized_base = String.downcase(base)
    base_signature = signature(normalized_base)

    Enum.filter(candidates, fn candidate ->
      normalized_candidate = String.downcase(candidate)

      normalized_candidate != normalized_base and
        signature(normalized_candidate) == base_signature
    end)
  end

  defp signature(word) do
    word
    |> String.to_charlist()
    |> Enum.sort()
  end
end
