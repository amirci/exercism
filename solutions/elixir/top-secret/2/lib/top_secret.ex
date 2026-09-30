defmodule TopSecret do
  def to_ast(string) do
    Code.string_to_quoted!(string)
  end

  def decode_secret_message_part({kind, _metadata, [signature | _]} = ast, acc)
      when kind in [:def, :defp] do
    {name, arguments} = get_function_name_and_args([signature])
    part = name |> Atom.to_string() |> String.slice(0, length(arguments))
    {ast, [part | acc]}
  end

  def decode_secret_message_part(ast, acc), do: {ast, acc}

  def decode_secret_message(string) do
    {_ast, parts} = string |> to_ast() |> Macro.prewalk([], &decode_secret_message_part/2)

    parts
    |> Enum.reverse()
    |> Enum.join()
  end

  defp get_function_name_and_args([{:when, _metadata, args} | _]),
    do: get_function_name_and_args(args)

  defp get_function_name_and_args([{name, _metadata, arguments} | _])
       when is_list(arguments),
       do: {name, arguments}

  defp get_function_name_and_args([{name, _metadata, _} | _]),
    do: {name, []}
end
