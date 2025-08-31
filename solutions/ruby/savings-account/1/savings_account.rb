module SavingsAccount
  def self.interest_rate(balance)
    return 3.213 if balance < 0

    case  
    when balance < 1_000
        0.5
    when balance < 5_000
        1.621
    else 
        2.475
    end
  end

  def self.annual_balance_update(balance)
    balance + balance * interest_rate(balance) / 100
  end

  def self.years_before_desired_balance(current_balance, desired_balance)
    accumulated_interest = Enumerator.new do |y|
      balance = current_balance
      loop do
        y << balance
        balance = annual_balance_update(balance)
      end
    end

    accumulated_interest.take_while { |b| b < desired_balance }.count

  end
end
