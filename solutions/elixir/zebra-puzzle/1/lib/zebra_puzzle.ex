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

  defp valid_colors?(colors), do: index(colors, :green) == index(colors, :ivory) + 1

  defp valid_nationalities(colors) do
    valid_permutations(@nationalities, fn nationalities ->
      Enum.at(nationalities, 0) == :norwegian and
        index(nationalities, :englishman) == index(colors, :red)
    end)
  end

  defp valid_drinks(colors, nationalities) do
    valid_permutations(@drinks, fn drinks ->
      Enum.at(drinks, 2) == :milk and
        Enum.at(drinks, index(colors, :green)) == :coffee and
        Enum.at(drinks, index(nationalities, :ukrainian)) == :tea
    end)
  end

  defp valid_pets(nationalities) do
    valid_permutations(@pets, fn pets ->
      Enum.at(pets, index(nationalities, :spaniard)) == :dog
    end)
  end

  defp valid_hobbies(colors, nationalities, drinks, pets) do
    valid_permutations(@hobbies, fn hobbies ->
      Enum.at(hobbies, index(pets, :snails)) == :dancing and
        Enum.at(hobbies, index(colors, :yellow)) == :painting and
        Enum.at(drinks, index(hobbies, :football)) == :orange_juice and
        Enum.at(hobbies, index(nationalities, :japanese)) == :chess and
        next_to?(hobbies, :reading, pets, :fox) and
        next_to?(hobbies, :painting, pets, :horse) and
        next_to?(nationalities, :norwegian, colors, :blue)
    end)
  end

  defp houses(colors, nationalities, pets, drinks, hobbies) do
    Enum.zip([colors, nationalities, pets, drinks, hobbies])
    |> Enum.map(fn {color, nationality, pet, drink, hobby} ->
      %{color: color, nationality: nationality, pet: pet, drink: drink, hobby: hobby}
    end)
  end

  defp next_to?(first_values, first, second_values, second) do
    abs(index(first_values, first) - index(second_values, second)) == 1
  end

  defp index(values, value), do: Enum.find_index(values, &(&1 == value))

  defp permutations([]), do: [[]]

  defp permutations(values) do
    for value <- values,
        rest <- permutations(values -- [value]),
        do: [value | rest]
  end
end
