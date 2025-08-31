module Port
  IDENTIFIER=:PALE

  def self.get_identifier(city)
    city[0..3].to_sym.upcase
  end

  def self.get_terminal(ship_identifier)
    target = ship_identifier.to_s[0..2]
    return :A if %w(OIL GAS).include? target
    :B
  end
end
