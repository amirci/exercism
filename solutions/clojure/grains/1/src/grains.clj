(ns grains
  (:require [clojure.math.numeric-tower :as math]))

(defn square
  [n]
  (math/expt 2 (dec n)))

(defn total
  []
  (reduce + (map square (range 1 65))))