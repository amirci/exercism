use Bitwise

defmodule Allergies do
  defp allergens do
    ~w[eggs peanuts shellfish strawberries tomatoes chocolate pollen cats] 
    |> Enum.with_index
    |> Map.new(fn {e, idx} -> {Integer.pow(2, idx), e} end)
  end
  
  @doc """
  List the allergies for which the corresponding flag bit is true.
  """
  @spec list(non_neg_integer) :: [String.t()]
  def list(flags) do
    allergens()
    |> Map.filter(fn {bit, _} -> (bit &&& flags) != 0 end)
    |> Map.values
    |> MapSet.new
  end

  @doc """
  Returns whether the corresponding flag bit in 'flags' is set for the item.
  """
  @spec allergic_to?(non_neg_integer, String.t()) :: boolean
  def allergic_to?(flags, item) do
    list(flags) |> MapSet.member?(item)
  end
end
