defmodule BasketballWebsite do
  def extract_from_path(data, path) do
    path
    |> String.split(".")
    |> Enum.reduce(data, fn p, acc -> acc[p] end)
  end

  def get_in_path(data, path) do
    path
    |> String.split(".")
    |> then(&(get_in(data, &1)))
  end
end
