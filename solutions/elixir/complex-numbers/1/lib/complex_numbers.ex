defmodule ComplexNumbers do
  @typedoc """
  In this module, complex numbers are represented as a tuple-pair containing the real and
  imaginary parts.
  For example, the real number `1` is `{1, 0}`, the imaginary number `i` is `{0, 1}` and
  the complex number `4+3i` is `{4, 3}'.
  """
  @type complex :: {number, number}

  @doc """
  Return the real part of a complex number
  """
  @spec real(a :: complex) :: number
  def real({real, _imaginary}), do: real

  @doc """
  Return the imaginary part of a complex number
  """
  @spec imaginary(a :: complex) :: number
  def imaginary({_real, imaginary}), do: imaginary

  @doc """
  Multiply two complex numbers, or a real and a complex number
  """
  @spec mul(a :: complex | number, b :: complex | number) :: complex
  def mul(a, b) do
    {a_real, a_imaginary} = normalize(a)
    {b_real, b_imaginary} = normalize(b)
    {a_real * b_real - a_imaginary * b_imaginary, a_real * b_imaginary + a_imaginary * b_real}
  end

  @doc """
  Add two complex numbers, or a real and a complex number
  """
  @spec add(a :: complex | number, b :: complex | number) :: complex
  def add(a, b) do
    {a_real, a_imaginary} = normalize(a)
    {b_real, b_imaginary} = normalize(b)
    {a_real + b_real, a_imaginary + b_imaginary}
  end

  @doc """
  Subtract two complex numbers, or a real and a complex number
  """
  @spec sub(a :: complex | number, b :: complex | number) :: complex
  def sub(a, b) do
    {a_real, a_imaginary} = normalize(a)
    {b_real, b_imaginary} = normalize(b)
    {a_real - b_real, a_imaginary - b_imaginary}
  end

  @doc """
  Divide two complex numbers, or a real and a complex number
  """
  @spec div(a :: complex | number, b :: complex | number) :: complex
  def div(a, b) do
    {a_real, a_imaginary} = normalize(a)
    {b_real, b_imaginary} = normalize(b)
    denominator = b_real * b_real + b_imaginary * b_imaginary

    {(a_real * b_real + a_imaginary * b_imaginary) / denominator,
     (a_imaginary * b_real - a_real * b_imaginary) / denominator}
  end

  @doc """
  Absolute value of a complex number
  """
  @spec abs(a :: complex) :: number
  def abs({real, imaginary}), do: :math.sqrt(real * real + imaginary * imaginary)

  @doc """
  Conjugate of a complex number
  """
  @spec conjugate(a :: complex) :: complex
  def conjugate({real, imaginary}), do: {real, -imaginary}

  @doc """
  Exponential of a complex number
  """
  @spec exp(a :: complex) :: complex
  def exp({real, imaginary}) do
    magnitude = :math.exp(real)
    {magnitude * :math.cos(imaginary), magnitude * :math.sin(imaginary)}
  end

  defp normalize(number) when is_number(number), do: {number, 0}
  defp normalize(complex), do: complex
end
