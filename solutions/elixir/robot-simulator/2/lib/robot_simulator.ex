defmodule RobotSimulator do
  @type robot() :: any()
  @type direction() :: :north | :east | :south | :west
  @type position() :: {integer(), integer()}

  @directions [:north, :east, :south, :west]
  @movements %{north: {0, 1}, east: {1, 0}, south: {0, -1}, west: {-1, 0}}

  @doc """
  Create a Robot Simulator given an initial direction and position.

  Valid directions are: `:north`, `:east`, `:south`, `:west`
  """
  @spec create(direction, position) :: robot() | {:error, String.t()}
  def create(direction \\ :north, position \\ {0, 0})

  def create(direction, {x, y}) when direction in @directions and is_integer(x) and is_integer(y) do
    %{direction: direction, position: {x, y}}
  end

  def create(direction, _position) when direction not in @directions, do: {:error, "invalid direction"}

  def create(_direction, _position), do: {:error, "invalid position"}

  @doc """
  Simulate the robot's movement given a string of instructions.

  Valid instructions are: "R" (turn right), "L", (turn left), and "A" (advance)
  """
  @spec simulate(robot, instructions :: String.t()) :: robot() | {:error, String.t()}
  def simulate(robot, instructions) do
    instructions
    |> String.to_charlist()
    |> Enum.reduce_while(robot, &move/2)
  end

  @doc """
  Return the robot's direction.

  Valid directions are: `:north`, `:east`, `:south`, `:west`
  """
  @spec direction(robot) :: direction()
  def direction(robot) do
    robot.direction
  end

  @doc """
  Return the robot's position.
  """
  @spec position(robot) :: position()
  def position(robot) do
    robot.position
  end

  defp move(?R, robot), do: {:cont, %{robot | direction: turn(robot.direction, 1)}}
  defp move(?L, robot), do: {:cont, %{robot | direction: turn(robot.direction, -1)}}

  defp move(?A, %{direction: direction, position: {x, y}} = robot) do
    {dx, dy} = @movements[direction]
    {:cont, %{robot | position: {x + dx, y + dy}}}
  end

  defp move(_instruction, _robot), do: {:halt, {:error, "invalid instruction"}}

  defp turn(direction, amount) do
    index = rem(Enum.find_index(@directions, &(&1 == direction)) + amount, 4)
    Enum.at(@directions, rem(index + 4, 4))
  end
end
