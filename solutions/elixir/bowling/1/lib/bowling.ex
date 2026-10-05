defmodule Bowling do
  defmodule Frame do
    defstruct [:start, :end]
  end

  defmodule Playing do
    defstruct rolls: [], frames: [], current_start: 0, current_rolls: []
  end

  defmodule Complete do
    defstruct rolls: [], frames: []
  end

  @type roll :: 0..10
  @type game :: %Playing{} | %Complete{}

  @spec start() :: game()
  def start, do: %Playing{}

  @spec roll(game(), integer()) :: {:ok, game()} | {:error, String.t()}
  def roll(_game, pins) when pins < 0, do: {:error, "Negative roll is invalid"}
  def roll(_game, pins) when pins > 10, do: {:error, "Pin count exceeds pins on the lane"}
  def roll(%Complete{}, _pins), do: {:error, "Cannot roll after game is over"}

  def roll(%Playing{} = game, pins) do
    if final_frame?(game) do
      roll_final(game, pins)
    else
      roll_regular(game, pins)
    end
  end

  @spec score(game()) :: {:ok, integer()} | {:error, String.t()}
  def score(%Complete{rolls: rolls, frames: frames}) do
    {:ok, Enum.sum(Enum.flat_map(frames, &frame_rolls(rolls, &1)))}
  end

  def score(%Playing{}), do: {:error, "Score cannot be taken until the end of the game"}

  defp roll_regular(%Playing{current_rolls: [], current_start: start} = game, 10) do
    {:ok, next_frame(game, 10, %Frame{start: start, end: start + 2}, start + 1)}
  end

  defp roll_regular(%Playing{current_rolls: []} = game, pins), do: {:ok, add_roll(game, pins)}

  defp roll_regular(%Playing{current_rolls: [first], current_start: start} = game, pins)
       when first + pins <= 10 do
    finish_frame = %Frame{start: start, end: start + if(first + pins == 10, do: 2, else: 1)}
    {:ok, next_frame(game, pins, finish_frame, start + 2)}
  end

  defp roll_regular(_game, _pins), do: {:error, "Pin count exceeds pins on the lane"}

  defp roll_final(%Playing{current_rolls: []} = game, pins), do: {:ok, add_roll(game, pins)}
  defp roll_final(%Playing{current_rolls: [first]} = game, pins) when first == 10,
    do: {:ok, add_roll(game, pins)}

  defp roll_final(%Playing{current_rolls: [first]} = game, pins) when first + pins == 10,
    do: {:ok, add_roll(game, pins)}

  defp roll_final(%Playing{current_rolls: [first], current_start: start} = game, pins)
       when first + pins < 10 do
    {:ok, complete(game, pins, %Frame{start: start, end: start + 1})}
  end

  defp roll_final(%Playing{current_rolls: [first, second], current_start: start} = game, pins)
       when first < 10 and first + second == 10 do
    {:ok, complete(game, pins, %Frame{start: start, end: start + 2})}
  end

  defp roll_final(%Playing{current_rolls: [10, second], current_start: start} = game, pins)
       when second + pins <= 10 do
    {:ok, complete(game, pins, %Frame{start: start, end: start + 2})}
  end

  defp roll_final(%Playing{current_rolls: [10, 10], current_start: start} = game, pins),
    do: {:ok, complete(game, pins, %Frame{start: start, end: start + 2})}

  defp roll_final(_game, _pins), do: {:error, "Pin count exceeds pins on the lane"}

  defp final_frame?(%Playing{frames: frames}), do: length(frames) == 9

  defp add_roll(%Playing{} = game, pins) do
    %{game | rolls: game.rolls ++ [pins], current_rolls: game.current_rolls ++ [pins]}
  end

  defp next_frame(game, pins, frame, next_start) do
    %Playing{
      rolls: game.rolls ++ [pins],
      frames: game.frames ++ [frame],
      current_start: next_start,
      current_rolls: []
    }
  end

  defp complete(game, pins, frame) do
    %Complete{rolls: game.rolls ++ [pins], frames: game.frames ++ [frame]}
  end

  defp frame_rolls(rolls, %Frame{start: start, end: ending}) do
    Enum.slice(rolls, start..ending)
  end
end
