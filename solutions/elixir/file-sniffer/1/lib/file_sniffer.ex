defmodule FileSniffer do
  @format_mismatch "Warning, file format and file extension do not match."

  @by_extension %{
    "exe" => "application/octet-stream",
    "bmp" => "image/bmp",
    "png" => "image/png",
    "gif" => "image/gif",
    "jpg" => "image/jpg"
  }

  def type_from_extension(extension) do
    @by_extension[extension]
  end

  def type_from_binary(<<0x42, 0x4D, _::binary>>), do: type_from_extension("bmp")
  def type_from_binary(<<0x47, 0x49, 0x46, _::binary>>), do: type_from_extension("gif")
  def type_from_binary(<<0xFF, 0xD8, 0xFF, _::binary>>), do: type_from_extension("jpg")
  def type_from_binary(<<0x7F, 0x45, 0x4C, 0x46, _::binary>>), do: type_from_extension("exe")
  def type_from_binary(<<0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, _::binary>>), do: type_from_extension("png")

  def type_from_binary(_), do: nil

  def verify(file_binary, extension) do
    case {type_from_extension(extension), type_from_binary(file_binary)} do
      {nil, _} -> {:error, @format_mismatch}
      {_, nil} -> {:error, @format_mismatch}
      {type, type} -> {:ok, type}
      _ -> {:error, @format_mismatch}
    end
  end
end
