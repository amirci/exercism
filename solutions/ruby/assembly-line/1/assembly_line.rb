class AssemblyLine
  def initialize(speed)
    @speed = speed
    @success_rate = if @speed == 10
      0.77
    elsif @speed == 9
      0.80
    elsif @speed > 4
      0.90
    else
      1
    end
  end

  SLOWEST_SPEED_PRODUCTION = 221
  
  def production_rate_per_hour
    SLOWEST_SPEED_PRODUCTION * @success_rate * @speed
  end

  def working_items_per_minute
    production_rate_per_hour.div(60)
  end
end
