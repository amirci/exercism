defmodule BoutiqueInventory do
  def sort_by_price(inventory) do
    Enum.sort_by(inventory, &(&1.price))
  end

  def with_missing_price(inventory) do
    Enum.filter(inventory, &(&1.price == nil))
  end

  def update_names(inventory, old_word, new_word) do
    inventory
    |> Enum.map(&(
        &1 |> 
        Map.replace(:name, String.replace(&1.name, old_word, new_word))
      ))
  end

  def increase_quantity(item, count) do
    item
    |> Map.update(
         :quantity_by_size, 
         %{}, 
         &(&1 |> Enum.reduce(%{}, fn {k, v}, acc -> Map.put(acc, k, v + count) end ))
    )
  end

  def total_quantity(item) do
    item.quantity_by_size
    |> Enum.reduce(0, fn {_, v}, acc -> acc + v end)
  end
end
