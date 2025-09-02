=begin
Write your code for the 'D&D Character' exercise in this file. Make the tests in
`dnd_character_test.rb` pass.

To get started with TDD, see the `README.md` file in your
`ruby/dnd-character` directory.
=end

class DndCharacter
  attr_reader :strength, :dexterity, :constitution, :intelligence, :wisdom, :charisma, :hitpoints
  
  BASE_HITPOINTS = 10
  
  def self.modifier(c)
    (c - BASE_HITPOINTS).div(2)
  end

  def initialize
    @strength, @dexterity, @constitution, @intelligence, @wisdom, @charisma = Array.new(6) { new_ability }
    @hitpoints = BASE_HITPOINTS + self.class.modifier(@constitution)
  end

  private
    def new_throw
      Array.new(4) { Random.rand(1..6) }
    end

    def new_ability
      new_throw.sort.drop(1).sum
    end
end
