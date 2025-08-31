(ns binary)

(defn to-decimal [s]
  (->> s
       (filter #(Character/isDigit %))
       (map #(Character/digit % 2))
       reverse
       (map * (iterate (partial * 2) 1))
       (apply +)))

