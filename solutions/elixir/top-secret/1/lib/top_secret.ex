defmodule TopSecret do
  def to_ast(string) do
    Code.string_to_quoted!(string)
  end

  def decode_secret_message_part({kind, _metadata, [signature | _]} = ast, acc)
      when kind in [:def, :defp] do
    {name, arity} = function_signature(signature)
    part = name |> Atom.to_string() |> String.slice(0, arity)
    {ast, [part | acc]}
  end

  def decode_secret_message_part(ast, acc), do: {ast, acc}

  def decode_secret_message(string) do
    {_ast, parts} = string |> to_ast() |> Macro.prewalk([], &decode_secret_message_part/2)

    parts
    |> Enum.reverse()
    |> Enum.join()
  end

  defp function_signature({:when, _metadata, [signature | _]}),
    do: function_signature(signature)

  defp function_signature({name, _metadata, arguments}) do
    {name, if(is_list(arguments), do: length(arguments), else: 0)}
  end
end
