(ns darts)

(defn hypo [x y]
  (Math/sqrt (+ (* x x) (* y y))))

(defn score
  "Calculates the score of a dart throw"
  [x y]
  (condp >= (hypo x y)
    1.0 10
    5.0 5
    10.0 1
    0))

