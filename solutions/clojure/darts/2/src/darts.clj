(ns darts
  (:require [clojure.math :as math]))

(defn score
  "Calculates the score of a dart throw"
  [x y]
  (condp >= (math/hypot x y)
    1 10
    5 5
    10 1
    0))

