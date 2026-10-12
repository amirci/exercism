defmodule ZebraPuzzle do
  @colors [:red, :green, :ivory, :yellow, :blue]
  @nationalities [:englishman, :spaniard, :ukrainian, :norwegian, :japanese]
  @pets [:dog, :snails, :fox, :horse, :zebra]
  @drinks [:coffee, :tea, :milk, :orange_juice, :water]
  @hobbies [:dancing, :painting, :reading, :football, :chess]

  @doc """
  Determine who drinks the water.
  """
  @spec drinks_water() :: atom()
  def drinks_water do
    solution()
    |> Enum.find(fn house -> house.drink == :water end)
    |> Map.fetch!(:nationality)
  end

  @doc """
  Determine who owns the zebra.
  """
  @spec owns_zebra() :: atom()
  def owns_zebra do
    solution()
    |> Enum.find(fn house -> house.pet == :zebra end)
    |> Map.fetch!(:nationality)
  end

  defp solution do
    solutions =
      for colors <- valid_permutations(@colors, &valid_colors?/1),
          nationalities <- valid_nationalities(colors),
          drinks <- valid_drinks(colors, nationalities),
          pets <- valid_pets(nationalities),
          hobbies <- valid_hobbies(colors, nationalities, drinks, pets),
          do: houses(colors, nationalities, pets, drinks, hobbies)

    Enum.find(solutions, & &1)
  end

  defp valid_permutations(values, predicate) do
    values
    |> permutations()
    |> Enum.filter(predicate)
  end

  defp valid_colors?(colors), do: left_of?(colors, :ivory, colors, :green)

  defp valid_nationalities(colors) do
    valid_permutations(@nationalities, fn nationalities ->
      index?(nationalities, :norwegian, 0) and
        coincident?(nationalities, :englishman, colors, :red)
    end)
  end

  defp valid_drinks(colors, nationalities) do
    valid_permutations(@drinks, fn drinks ->
      Enum.at(drinks, 2) == :milk and
        coincident?(colors, :green, drinks, :coffee) and
        coincident?(nationalities, :ukrainian, drinks, :tea)
    end)
  end

  defp valid_pets(nationalities) do
    valid_permutations(@pets, fn pets ->
      coincident?(nationalities, :spaniard, pets, :dog)
    end)
  end

  defp valid_hobbies(colors, nationalities, drinks, pets) do
    valid_permutations(@hobbies, fn hobbies ->
      coincident?(pets, :snails, hobbies, :dancing) and
        coincident?(colors, :yellow, hobbies, :painting) and
        coincident?(hobbies, :football, drinks, :orange_juice) and
        coincident?(nationalities, :japanese, hobbies, :chess) and
        adjacent?(hobbies, :reading, pets, :fox) and
        adjacent?(hobbies, :painting, pets, :horse) and
        adjacent?(nationalities, :norwegian, colors, :blue)
    end)
  end

  defp houses(colors, nationalities, pets, drinks, hobbies) do
    Enum.zip([colors, nationalities, pets, drinks, hobbies])
    |> Enum.map(fn {color, nationality, pet, drink, hobby} ->
      %{color: color, nationality: nationality, pet: pet, drink: drink, hobby: hobby}
    end)
  end

  defp index?(values, value, target), do: index(values, value) == target

  defp coincident?(from, value, target, expected),
    do: Enum.at(target, index(from, value)) == expected

  defp left_of?(from, value, target, expected),
    do: Enum.at(target, index(from, value) + 1) == expected

  defp adjacent?(from, value, target, expected),
    do: abs(index(from, value) - index(target, expected)) == 1

  defp index(values, value), do: Enum.find_index(values, &(&1 == value))

  defp permutations([]), do: [[]]

  defp permutations(values) do
    for value <- values,
        rest <- permutations(values -- [value]),
        do: [value | rest]
  end
end
