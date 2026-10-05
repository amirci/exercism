defmodule Bowling do
  defmodule Frame do
    defstruct [:start, :end]
  end

  defmodule Regular do
    defstruct rolls: [], frames: [], current_start: 0, current_rolls: []
  end

  defmodule LastFrame do
    defstruct rolls: [], frames: [], current_start: 0, current_rolls: []
  end

  defmodule Complete do
    defstruct rolls: [], frames: []
  end

  @type roll :: 0..10
  @type game :: %Regular{} | %LastFrame{} | %Complete{}

  @spec start() :: game()
  def start, do: %Regular{}

  @spec roll(game(), integer()) :: {:ok, game()} | {:error, String.t()}
  def roll(_game, pins) when pins < 0, do: {:error, "Negative roll is invalid"}
  def roll(_game, pins) when pins > 10, do: {:error, "Pin count exceeds pins on the lane"}
  def roll(%Complete{}, _pins), do: {:error, "Cannot roll after game is over"}
  def roll(%LastFrame{} = game, pins), do: roll_last(game, pins)

  def roll(%Regular{} = game, pins), do: roll_regular(game, pins)

  @spec score(game()) :: {:ok, integer()} | {:error, String.t()}
  def score(%Complete{rolls: rolls, frames: frames}) do
    {:ok, Enum.sum(Enum.flat_map(frames, &frame_rolls(rolls, &1)))}
  end

  def score(%Regular{}), do: {:error, "Score cannot be taken until the end of the game"}
  def score(%LastFrame{}), do: {:error, "Score cannot be taken until the end of the game"}

  defp roll_regular(%Regular{current_rolls: [], current_start: start} = game, 10) do
    {:ok, next_frame(game, 10, %Frame{start: start, end: start + 2}, start + 1)}
  end

  defp roll_regular(%Regular{current_rolls: []} = game, pins), do: {:ok, add_roll(game, pins)}

  defp roll_regular(%Regular{current_rolls: [first], current_start: start} = game, pins)
       when first + pins <= 10 do
    finish_frame = %Frame{start: start, end: start + if(first + pins == 10, do: 2, else: 1)}
    {:ok, next_frame(game, pins, finish_frame, start + 2)}
  end

  defp roll_regular(_game, _pins), do: {:error, "Pin count exceeds pins on the lane"}

  defp roll_last(%LastFrame{} = game, pins) do
    case valid_last_roll?(game.current_rolls, pins) do
      true -> {:ok, apply_last_roll(game, pins)}
      false -> {:error, "Pin count exceeds pins on the lane"}
    end
  end

  defp valid_last_roll?([], _pins), do: true
  defp valid_last_roll?([10], _pins), do: true
  defp valid_last_roll?([first], pins), do: first + pins <= 10
  defp valid_last_roll?([first, second], _pins) when first < 10, do: first + second == 10
  defp valid_last_roll?([10, 10], _pins), do: true
  defp valid_last_roll?([10, second], pins), do: second + pins <= 10
  defp valid_last_roll?(_, _pins), do: false

  defp apply_last_roll(%LastFrame{current_rolls: []} = game, pins), do: add_roll(game, pins)
  defp apply_last_roll(%LastFrame{current_rolls: [10]} = game, pins), do: add_roll(game, pins)

  defp apply_last_roll(%LastFrame{current_rolls: [first]} = game, pins)
       when first + pins == 10 do
    add_roll(game, pins)
  end

  defp apply_last_roll(%LastFrame{current_rolls: [first], current_start: start} = game, pins)
       when first + pins < 10 do
    complete(game, pins, %Frame{start: start, end: start + 1})
  end

  defp apply_last_roll(%LastFrame{current_rolls: [first, _second], current_start: start} = game, pins)
       when first < 10 do
    complete(game, pins, %Frame{start: start, end: start + 2})
  end

  defp apply_last_roll(%LastFrame{current_start: start} = game, pins),
    do: complete(game, pins, %Frame{start: start, end: start + 2})

  defp add_roll(game, pins) do
    %{game | rolls: game.rolls ++ [pins], current_rolls: game.current_rolls ++ [pins]}
  end

  defp next_frame(game, pins, frame, next_start) do
    state = if length(game.frames) + 1 == 9, do: LastFrame, else: Regular

    struct(state,
      rolls: game.rolls ++ [pins],
      frames: game.frames ++ [frame],
      current_start: next_start,
      current_rolls: []
    )
  end

  defp complete(game, pins, frame) do
    %Complete{rolls: game.rolls ++ [pins], frames: game.frames ++ [frame]}
  end

  defp frame_rolls(rolls, %Frame{start: start, end: ending}) do
    Enum.slice(rolls, start..ending)
  end
end
