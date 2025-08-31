(ns square-root)

(defn square-root
  "Calculates a number's square root"
  [n]
  (->> n
       (iterate #(float (/ (+ % (/ n %)) 2)))
       (partition 2 1)
       (take-while (fn [[a b]] (not= a b)))
       (last)
       (first)
       int))


