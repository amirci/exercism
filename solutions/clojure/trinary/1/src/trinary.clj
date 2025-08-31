(ns trinary)

(def base 3)

(defn to-decimal [s]
  (if-not (every? #(Character/isDigit %) s)
    0
    (->> s
         (map #(Character/digit % base))
         reverse
         (map * (iterate (partial * base) 1))
         (apply +))))

