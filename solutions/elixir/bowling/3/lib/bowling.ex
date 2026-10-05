defmodule Bowling do
  defmodule Frame do
    defstruct [:start, :end]
  end

  defmodule FirstRoll do
    defstruct [:start]
  end

  defmodule SecondRoll do
    defstruct [:start, :first]
  end

  defmodule FirstTwoExtraRolls do
    defstruct [:start]
  end

  defmodule SecondTwoExtraRolls do
    defstruct [:start, :first]
  end

  defmodule OneExtraRoll do
    defstruct [:start]
  end

  defmodule Regular do
    defstruct rolls: [], frames: [], current_frame: %FirstRoll{start: 0}
  end

  defmodule LastFrame do
    defstruct rolls: [], frames: [], current_frame: %FirstRoll{start: 0}
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
  def roll(%Regular{current_frame: %SecondRoll{first: first}}, pins) when first + pins > 10,
    do: {:error, "Pin count exceeds pins on the lane"}
  def roll(%LastFrame{current_frame: %SecondRoll{first: first}}, pins) when first + pins > 10,
    do: {:error, "Pin count exceeds pins on the lane"}
  def roll(%LastFrame{current_frame: %SecondTwoExtraRolls{first: first}}, pins)
      when first < 10 and first + pins > 10,
      do: {:error, "Pin count exceeds pins on the lane"}

  def roll(game, pins) do
    {:ok, roll_it(%{game | rolls: game.rolls ++ [pins]}, pins)}
  end

  defp roll_it(%Regular{current_frame: %FirstRoll{start: start}} = game, 10) do
    frame = %Frame{start: start, end: start + 2}
    complete_frame(game, frame, start + 1)
  end

  defp roll_it(%Regular{current_frame: %FirstRoll{start: start}} = game, pins) do
    %{game | current_frame: %SecondRoll{start: start, first: pins}}
  end

  defp roll_it(%Regular{current_frame: %SecondRoll{start: start, first: first}} = game, pins) do
    ending = start + if(first + pins == 10, do: 2, else: 1)
    frame = %Frame{start: start, end: ending}
    complete_frame(game, frame, start + 2)
  end

  defp roll_it(%LastFrame{current_frame: %FirstRoll{start: start}} = game, 10) do
    %{game | current_frame: %FirstTwoExtraRolls{start: start}}
  end

  defp roll_it(%LastFrame{current_frame: %FirstRoll{start: start}} = game, pins) do
    %{game | current_frame: %SecondRoll{start: start, first: pins}}
  end

  defp roll_it(%LastFrame{current_frame: %SecondRoll{start: start, first: first}} = game, pins)
       when first + pins == 10 do
    %{game | current_frame: %OneExtraRoll{start: start}}
  end

  defp roll_it(%LastFrame{current_frame: %SecondRoll{start: start}} = game, pins) do
    complete(game, pins, %Frame{start: start, end: start + 1})
  end

  defp roll_it(%LastFrame{current_frame: %FirstTwoExtraRolls{start: start}} = game, pins) do
    %{game | current_frame: %SecondTwoExtraRolls{start: start, first: pins}}
  end

  defp roll_it(%LastFrame{current_frame: %SecondTwoExtraRolls{start: start}} = game, pins),
    do: complete(game, pins, %Frame{start: start, end: start + 2})

  defp roll_it(%LastFrame{current_frame: %OneExtraRoll{start: start}} = game, pins),
    do: complete(game, pins, %Frame{start: start, end: start + 2})

  @spec score(game()) :: {:ok, integer()} | {:error, String.t()}
  def score(%Complete{rolls: rolls, frames: frames}),
    do: {:ok, Enum.sum(Enum.flat_map(frames, &frame_rolls(rolls, &1)))}

  def score(_game), do: {:error, "Score cannot be taken until the end of the game"}

  defp complete_frame(game, frame, next_start) do
    frame_kind = if length(game.frames) + 1 == 9, do: LastFrame, else: Regular

    struct(frame_kind,
      rolls: game.rolls,
      frames: game.frames ++ [frame],
      current_frame: %FirstRoll{start: next_start}
    )
  end

  defp complete(game, _pins, frame),
    do: %Complete{rolls: game.rolls, frames: game.frames ++ [frame]}

  defp frame_rolls(rolls, %Frame{start: start, end: ending}), do: Enum.slice(rolls, start..ending)
end
