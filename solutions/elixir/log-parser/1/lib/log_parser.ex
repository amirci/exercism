defmodule LogParser do
  def valid_line?(line) do
    # Regex.run(~r/(\d) apples/, "Alice has 7 apples"
    line =~ ~r/\[(DEBUG|INFO|WARNING|ERROR)\] .+/
  end

  def split_line(line) do
    line |> String.split(~r/<[\-\*\~=]*>/)
  end

  def remove_artifacts(line) do
    line |> String.replace(~r/end-of-line\d+/i, "")
  end

  def tag_with_user_name(line) do
    match = Regex.run(~r/User[\s\t]+(\S+).*/, line)
    case match do
      nil -> line
      [_, user_name] -> "[USER] #{user_name} #{line}"
    end
  end
end
