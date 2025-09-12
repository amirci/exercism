defmodule LibraryFees do
  def datetime_from_string(s) do
    {_, d} = NaiveDateTime.from_iso8601(s)
    d
  end

  def before_noon?(datetime) do
    NaiveDateTime.to_time(datetime) < ~T[12:00:00]
  end

  def return_date(checkout_datetime) do
    extra = if before_noon?(checkout_datetime), do: 0, else: 1
    checkout_datetime
    |> NaiveDateTime.to_date
    |> Date.add(28 + extra)
  end

  def days_late(planned_return_date, actual_date) do
    actual_date
    |> NaiveDateTime.to_date
    |> Date.diff(planned_return_date)
    |> max(0)
  end

  def monday?(datetime) do
    datetime
    |> NaiveDateTime.to_date
    |> Date.day_of_week
    |> Kernel.==(1)
  end

  def calculate_late_fee(checkout, return, rate) do
    return = datetime_from_string(return)
    factor = if monday?(return), do: 0.5, else: 1
    
    checkout
    |> datetime_from_string
    |> return_date
    |> days_late(return)
    |> Kernel.*(rate)
    |> Kernel.*(factor)    
    |> trunc
  end
end
