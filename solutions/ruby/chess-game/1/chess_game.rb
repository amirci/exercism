module Chess
  RANKS = 1..8
  FILES = 'A'..'H'

  def self.valid_square?(rank, file)
    RANKS.include?(rank) && FILES.include?(file)
  end

  def self.nick_name(first_name, last_name)
    (first_name[..1] + last_name[-2..]).upcase
  end

  def self.move_message(first_name, last_name, square)
    nick = nick_name(first_name, last_name)
    return "#{nick} moved to #{square}" if valid_square?(square[1].to_i, square[0])
    "#{nick} attempted to move to #{square}, but that is not a valid square" 
  end
end
