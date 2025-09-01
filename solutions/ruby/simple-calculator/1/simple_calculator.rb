class SimpleCalculator
  ALLOWED_OPERATIONS = ['+', '/', '*'].freeze

  class UnsupportedOperation < StandardError
  end
  
  def self.calculate(first_operand, second_operand, operation)
    raise UnsupportedOperation.new("Operation #{operation} is not supported") unless %w(+ / *).include?(operation)

    begin
      result = operation.to_sym.to_proc.call(first_operand, second_operand)
      "#{first_operand} #{operation} #{second_operand} = #{result}"
    rescue ZeroDivisionError => _
      "Division by zero is not allowed."
    rescue TypeError => _
      raise ArgumentError.new
    end
  end
end
