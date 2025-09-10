defmodule Strain do
  @doc """
  Given a `list` of items and a function `fun`, return the list of items where
  `fun` returns true.

  Do not use `Enum.filter`.
  """
  @spec keep(list :: list(any), fun :: (any -> boolean)) :: list(any)
  def keep(list, fun) do
    keep_it([], list, fun)
  end

  defp keep_it(result, [], _), do: result
  defp keep_it(result, [fst | rst], f) do
    new_result = if f.(fst) do 
      result ++ [fst]
    else
      result
    end
    keep_it(new_result, rst, f)
  end

  @doc """
  Given a `list` of items and a function `fun`, return the list of items where
  `fun` returns false.

  Do not use `Enum.reject`.
  """
  @spec discard(list :: list(any), fun :: (any -> boolean)) :: list(any)
  def discard(list, fun) do
    keep(list, fn x -> !fun.(x) end)
  end
end
