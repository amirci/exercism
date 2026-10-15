defmodule Meetup do
  @moduledoc """
  Calculate meetup dates.
  """

  @type weekday ::
          :monday
          | :tuesday
          | :wednesday
          | :thursday
          | :friday
          | :saturday
          | :sunday

  @type schedule :: :first | :second | :third | :fourth | :last | :teenth

  @weekday_numbers %{
    monday: 1,
    tuesday: 2,
    wednesday: 3,
    thursday: 4,
    friday: 5,
    saturday: 6,
    sunday: 7
  }

  @doc """
  Calculate a meetup date.

  The schedule is in which week (1..4, last or "teenth") the meetup date should
  fall.
  """
  @spec meetup(pos_integer, pos_integer, weekday, schedule) :: Date.t()
  def meetup(year, month, weekday, schedule) do
    dates_for_weekday(year, month, weekday)
    |> select_date(schedule)
  end

  defp dates_for_weekday(year, month, weekday) do
    first_day = Date.new!(year, month, 1)
    last_day = Date.end_of_month(first_day)

    Date.range(first_day, last_day)
    |> Enum.filter(&(Date.day_of_week(&1) == @weekday_numbers[weekday]))
  end

  defp teenth?(%Date{day: day}), do: day in 13..19

  defp matcher(:first), do: &List.first/1
  defp matcher(:second), do: &Enum.at(&1, 1)
  defp matcher(:third), do: &Enum.at(&1, 2)
  defp matcher(:fourth), do: &Enum.at(&1, 3)
  defp matcher(:last), do: &List.last/1
  defp matcher(:teenth), do: fn dates -> Enum.find(dates, &teenth?/1) end

  defp select_date(dates, schedule), do: matcher(schedule).(dates)
end
