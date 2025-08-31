(ns cars-assemble)

(defn production-rate
  "Returns the assembly line's production rate per hour,
   taking into account its success rate"
  [speed]
  (->> (condp <= speed
         10 0.77
         9  0.8
         5  0.9
         1)
       (* 221.0 speed)))


(defn working-items
  "Calculates how many working cars are produced per minute"
  [speed]
  (int (quot (production-rate speed) 60)))
