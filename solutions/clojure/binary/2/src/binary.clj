(ns binary)

(defn to-decimal [s]
  (if-not (every? #(Character/isDigit %) s)
    0
    (->> s
         (map #(Character/digit % 2))
         reverse
         (map * (iterate (partial * 2) 1))
         (apply +))))

