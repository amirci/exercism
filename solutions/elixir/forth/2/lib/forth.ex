defmodule Forth do
  @opaque interpreter_state :: %{stack: [integer()], words: %{String.t() => [tuple()]}}

  @doc """
  Create a new interpreter state.
  """
  @spec new() :: interpreter_state()
  def new do
    %{stack: [], words: builtin_words()}
  end

  @doc """
  Evaluate an input string, updating the interpreter state.
  """
  @spec eval(interpreter_state(), String.t()) :: interpreter_state()
  def eval(state, input) do
    input
    |> tokens()
    |> execute(state)
  end

  @doc """
  Return the current stack as a string with the element on top of the stack
  being the rightmost element in the string.
  """
  @spec format_stack(interpreter_state()) :: String.t()
  def format_stack(%{stack: stack}), do: stack |> Enum.reverse() |> Enum.join(" ")

  defp execute([], state), do: state

  defp execute([":" | tokens], state) do
    case parse_definition(tokens) do
      {:ok, {name, body, rest}} ->
        compiled = Enum.flat_map(body, &compile_token(&1, state.words))
        key = normalize(name)
        execute(rest, %{state | words: Map.put(state.words, key, compiled)})

      {:error, word} ->
        raise __MODULE__.InvalidWord, word: word
    end
  end

  defp execute([token | rest], state) do
    instructions = compile_token(token, state.words)
    state = execute_instructions(instructions, state)
    execute(rest, state)
  end

  defp tokens(input), do: String.split(input, ~r/[\p{Z}\p{C}]+/u, trim: true)

  defp parse_definition(tokens) do
    case Enum.split_while(tokens, &(&1 != ";")) do
      {_, []} ->
        {:error, ":"}

      {[], _} ->
        {:error, ":"}

      {[name | body], [_separator | rest]} ->
        if number?(name) do
          {:error, name}
        else
          {:ok, {name, body, rest}}
        end
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
          true -> raise __MODULE__.UnknownWord, word: token
        end
    end
  end

  defp execute_instructions([], state), do: state

  defp execute_instructions([{:push, value} | rest], state) do
    execute_instructions(rest, %{state | stack: [value | state.stack]})
  end

  defp execute_instructions([{:op, operation} | rest], state) do
    execute_instructions(rest, apply_operation(state, operation))
  end

  defp apply_operation(state, operation),
    do: %{state | stack: operation.(state.stack)}

  defp builtin_words do
    %{
      "+" => [{:op, &Forth.StackOperation.add/1}],
      "-" => [{:op, &Forth.StackOperation.sub/1}],
      "*" => [{:op, &Forth.StackOperation.mul/1}],
      "/" => [{:op, &Forth.StackOperation.div/1}],
      "dup" => [{:op, &Forth.StackOperation.dup/1}],
      "drop" => [{:op, &Forth.StackOperation.drop/1}],
      "swap" => [{:op, &Forth.StackOperation.swap/1}],
      "over" => [{:op, &Forth.StackOperation.over/1}]
    }
  end

  defp number?(word), do: match?({_, ""}, Integer.parse(word))
  defp normalize(word), do: String.downcase(word)

  defmodule StackOperation do
    def add([right, left | rest]), do: [left + right | rest]
    def add(_), do: raise(Forth.StackUnderflow)

    def sub([right, left | rest]), do: [left - right | rest]
    def sub(_), do: raise(Forth.StackUnderflow)

    def mul([right, left | rest]), do: [left * right | rest]
    def mul(_), do: raise(Forth.StackUnderflow)

    def div([0, _left | _rest]), do: raise(Forth.DivisionByZero)
    def div([right, left | rest]), do: [Kernel.div(left, right) | rest]
    def div(_), do: raise(Forth.StackUnderflow)

    def dup([top | rest]), do: [top, top | rest]
    def dup(_), do: raise(Forth.StackUnderflow)

    def drop([_top | rest]), do: rest
    def drop(_), do: raise(Forth.StackUnderflow)

    def swap([top, below | rest]), do: [below, top | rest]
    def swap(_), do: raise(Forth.StackUnderflow)

    def over([top, below | rest]), do: [below, top, below | rest]
    def over(_), do: raise(Forth.StackUnderflow)
  end

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
