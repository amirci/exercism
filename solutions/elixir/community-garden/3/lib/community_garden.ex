# Use the Plot struct as it is provided
defmodule Plot do
  @enforce_keys [:plot_id, :registered_to]
  defstruct [:plot_id, :registered_to]

  def new(plot_id, registered_to) do
    %__MODULE__{plot_id: plot_id, registered_to: registered_to}
  end
end

defmodule CommunityGarden do
  def start(opts \\ []) do
    Agent.start(fn -> %{next_id: 1, plots: []} end, opts)
  end

  def list_registrations(pid) do
    Agent.get(pid, & &1.plots)
  end

  def register(pid, register_to) do
    Agent.get_and_update(pid, &register_plot(&1, register_to))
  end

  def release(pid, plot_id) do
    Agent.update(pid, fn st -> %{st | plots: remove_plot(st.plots, plot_id)} end)
  end

  def get_registration(pid, plot_id) do
    Agent.get(pid, fn state -> find_plot(state.plots, plot_id) end)
  end

  defp register_plot(%{next_id: next_id, plots: plots} = state, register_to) do
    plot = Plot.new(next_id, register_to)
    {plot, %{state | next_id: next_id + 1, plots: plots ++ [plot]}}
  end

  defp remove_plot(plots, plot_id) do
    Enum.reject(plots, &(&1.plot_id == plot_id))
  end

  defp find_plot(plots, plot_id) do
    Enum.find(
      plots,
      {:not_found, "plot is unregistered"},
      &(&1.plot_id == plot_id)
    )
  end
end
