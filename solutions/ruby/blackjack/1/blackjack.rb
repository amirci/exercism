module Blackjack
  def self.parse_card(card)
    case card
      when 'ace' then 11
      when 'king', 'queen', 'jack', 'ten' then 10
      when 'nine' then 9
      when 'eight' then 8
      when 'seven' then 7
      when 'six' then 6
      when 'five' then 5
      when 'four' then 4
      when 'three' then 3
      when 'two' then 2
    else 0
    end
  end

  def self.card_range(card1, card2)
    total = [card1, card2].map { |c| parse_card(c) }.sum()
    case total
      when 4..11 then 'low'
      when 12..16 then 'mid'
      when 17..20 then 'high'
      when 21 then 'blackjack'
    else 
      total
    end
  end

  def self.first_turn(card1, card2, dealer_card)
    return 'P' if card1 == card2 && card2 == 'ace'
    dealer = parse_card(dealer_card)
    case card_range(card1, card2)
      when ->(r) { r == 'blackjack' and dealer < 10 } then 'W'
      when 'blackjack','high' then 'S'
      when ->(r) { r == 'mid' && dealer > 6 } then 'H'
      when 'mid' then 'S'
    else 
      'H'
    end
  end
end
