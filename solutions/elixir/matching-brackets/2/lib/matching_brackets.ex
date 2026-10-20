defmodule MatchingBrackets do
  @pairs %{?( => ?), ?[ => ?], ?{ => ?}}

  @doc """
  Checks that all the brackets and braces in the string are matched correctly, and nested correctly
  """
  @spec check_brackets(String.t()) :: boolean
  def check_brackets(str) do
    str
    |> to_charlist()
    |> Enum.reduce_while([], &process_character/2)
    |> Kernel.==([])
  end

  defp process_character(character, stack) do
    case bracket_type(character) do
      {:opening, expected} ->
        {:cont, [expected | stack]}

      {:closing, closing} ->
        close_bracket(closing, stack)

      :other ->
        {:cont, stack}
    end
  end

  defp bracket_type(character) do
    cond do
      Map.has_key?(@pairs, character) -> {:opening, Map.fetch!(@pairs, character)}
      character in Map.values(@pairs) -> {:closing, character}
      true -> :other
    end
  end

  defp close_bracket(closing, [top | rest]) when top == closing, do: {:cont, rest}
  defp close_bracket(_closing, _stack), do: {:halt, :invalid}
end
