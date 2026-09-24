defmodule Newsletter do
  def read_emails(path) do
    path
    |> File.read!()
    |> String.split("\n", trim: true)
  end

  def open_log(path) do
    File.open!(path, [:write])
  end

  def log_sent_email(pid, email) do
    IO.write(pid, email <> "\n")
  end

  def close_log(pid) do
    File.close(pid)
  end

  def send_newsletter(emails_path, log_path, send_fun) do
    log = open_log(log_path)

    try do
      emails_path
      |> read_emails()
      |> Enum.each(fn email ->
        if send_fun.(email) == :ok do
          log_sent_email(log, email)
        end
      end)

      :ok
    after
      close_log(log)
    end
  end
end
