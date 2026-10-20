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
    case @pairs[character] do
      nil -> process_closing_or_ignored(character, stack)
      expected -> {:cont, [expected | stack]}
    end
  end

  defp process_closing_or_ignored(character, stack) do
    if character in Map.values(@pairs) do
      case stack do
        [^character | rest] -> {:cont, rest}
        _ -> {:halt, :invalid}
      end
    else
      {:cont, stack}
    end
  end
end
