defmodule Anagram do
  @doc """
  Returns all candidates that are anagrams of, but not equal to, 'base'.
  """
  @spec match(String.t(), [String.t()]) :: [String.t()]
  def match(base, candidates) do
    normalized_base = String.downcase(base)
    base_signature = signature(normalized_base)

    anagram? = fn
      ^normalized_base -> false
      candidate -> signature(candidate) == base_signature
    end

    Enum.filter(candidates, fn candidate -> anagram?.(String.downcase(candidate)) end)
  end

  defp signature(word) do
    word
    |> String.to_charlist()
    |> Enum.sort()
  end
end
