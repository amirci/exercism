(ns grains)

(defn square
  [n]
  (cond-> n
    (< 2 n) (->> dec (.pow (biginteger 2)))))

(defn total
  []
  (->> (range 1 65)
       (map square)
       (apply +)))

