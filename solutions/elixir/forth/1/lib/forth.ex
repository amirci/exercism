defmodule Forth do
  @opaque evaluator :: %{stack: [integer()], words: %{String.t() => [tuple()]}}
  @builtin_names ["+", "-", "*", "/", "dup", "drop", "swap", "over"]

  @doc """
  Create a new evaluator.
  """
  @spec new() :: evaluator()
  def new, do: %{stack: [], words: %{}}

  @doc """
  Evaluate an input string, updating the evaluator state.
  """
  @spec eval(evaluator(), String.t()) :: evaluator()
  def eval(evaluator, input) do
    input
    |> tokens()
    |> execute(evaluator)
  end

  @doc """
  Return the current stack as a string with the element on top of the stack
  being the rightmost element in the string.
  """
  @spec format_stack(evaluator()) :: String.t()
  def format_stack(%{stack: stack}), do: stack |> Enum.reverse() |> Enum.join(" ")

  defp execute([], evaluator), do: evaluator

  defp execute([":" | tokens], evaluator) do
    {name, body, rest} = parse_definition(tokens)
    key = normalize(name)

    if number?(name) do
      raise __MODULE__.InvalidWord, word: name
    end

    compiled = Enum.flat_map(body, &compile_token(&1, evaluator.words))
    execute(rest, %{evaluator | words: Map.put(evaluator.words, key, compiled)})
  end

  defp execute([token | rest], evaluator) do
    instructions = compile_token(token, evaluator.words)
    evaluator = execute_instructions(instructions, evaluator)
    execute(rest, evaluator)
  end

  defp tokens(input), do: String.split(input, ~r/[\p{Z}\p{C}]+/u, trim: true)

  defp parse_definition(tokens) do
    case Enum.split_while(tokens, &(&1 != ";")) do
      {_, []} -> raise __MODULE__.InvalidWord, word: ":"
      {[], _} -> raise __MODULE__.InvalidWord, word: ":"
      {[name | body], [_separator | rest]} -> {name, body, rest}
    end
  end

  defp compile_token(token, words) do
    case Integer.parse(token) do
      {number, ""} ->
        [{:push, number}]

      _ ->
        key = normalize(token)

        cond do
          Map.has_key?(words, key) -> Map.fetch!(words, key)
          key in @builtin_names -> [{:op, key}]
          true -> raise __MODULE__.UnknownWord, word: token
        end
    end
  end

  defp execute_instructions([], evaluator), do: evaluator

  defp execute_instructions([{:push, value} | rest], evaluator) do
    execute_instructions(rest, %{evaluator | stack: [value | evaluator.stack]})
  end

  defp execute_instructions([{:op, operation} | rest], evaluator) do
    execute_instructions(rest, apply_operation(evaluator, operation))
  end

  defp apply_operation(evaluator, "+"), do: binary_operation(evaluator, &Kernel.+/2)
  defp apply_operation(evaluator, "-"), do: binary_operation(evaluator, &Kernel.-/2)
  defp apply_operation(evaluator, "*"), do: binary_operation(evaluator, &Kernel.*/2)

  defp apply_operation(%{stack: [0, _left | _rest]}, "/"),
    do: raise(__MODULE__.DivisionByZero)

  defp apply_operation(evaluator, "/"), do: binary_operation(evaluator, &div/2)

  defp apply_operation(%{stack: [top | rest]} = evaluator, "dup"),
    do: %{evaluator | stack: [top, top | rest]}

  defp apply_operation(%{stack: [_top | rest]} = evaluator, "drop"),
    do: %{evaluator | stack: rest}

  defp apply_operation(%{stack: [top, below | rest]} = evaluator, "swap"),
    do: %{evaluator | stack: [below, top | rest]}

  defp apply_operation(%{stack: [top, below | rest]} = evaluator, "over"),
    do: %{evaluator | stack: [below, top, below | rest]}

  defp apply_operation(_evaluator, _operation), do: raise(__MODULE__.StackUnderflow)

  defp binary_operation(%{stack: [right, left | rest]} = evaluator, operation),
    do: %{evaluator | stack: [operation.(left, right) | rest]}

  defp binary_operation(_evaluator, _operation), do: raise(__MODULE__.StackUnderflow)

  defp number?(word), do: match?({_, ""}, Integer.parse(word))
  defp normalize(word), do: String.downcase(word)

  defmodule StackUnderflow do
    defexception []
    def message(_), do: "stack underflow"
  end

  defmodule InvalidWord do
    defexception word: nil
    def message(e), do: "invalid word: #{inspect(e.word)}"
  end

  defmodule UnknownWord do
    defexception word: nil
    def message(e), do: "unknown word: #{inspect(e.word)}"
  end

  defmodule DivisionByZero do
    defexception []
    def message(_), do: "division by zero"
  end
end
