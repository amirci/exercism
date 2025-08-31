(ns triangle)

(defn count-diff-sides [a b c]
  (let [[a b c :as sorted] (sort [a b c])]
    (when (and
           (every? pos? sorted)
           (< c (+ a b)))
      (count (set sorted)))))

(defn eq-sides [op n a b c]
  (let [cnt (count-diff-sides a b c)]
    (and (not (nil? cnt))
         (op n cnt))))

(def equilateral? (partial eq-sides = 1))

(def isosceles? (partial eq-sides >= 2))

(def scalene? (partial eq-sides = 3))
