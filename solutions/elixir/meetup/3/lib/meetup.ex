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

  @schedule_indices %{first: 0, second: 1, third: 2, fourth: 3}

  @doc """
  Calculate a meetup date.

  The schedule is in which week (1..4, last or "teenth") the meetup date should
  fall.
  """
  @spec meetup(pos_integer, pos_integer, weekday, schedule) :: Date.t()
  def meetup(year, month, weekday, schedule) do
    dates = dates_for_weekday(year, month, weekday)

    case schedule do
      :teenth -> Enum.find(dates, &teenth?/1)
      :last -> List.last(dates)
      _ -> scheduled_date(dates, schedule)
    end
  end

  defp dates_for_weekday(year, month, weekday) do
    first_day = Date.new!(year, month, 1)
    last_day = Date.end_of_month(first_day)

    Date.range(first_day, last_day)
    |> Enum.filter(&(Date.day_of_week(&1) == @weekday_numbers[weekday]))
  end

  defp teenth?(%Date{day: day}), do: day in 13..19

  defp scheduled_date(dates, schedule) do
    Enum.at(dates, Map.fetch!(@schedule_indices, schedule))
  end
end
