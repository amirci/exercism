(ns pascals-triangle)


(defn calc-next-row [last-row]
  "right and left of the previous row"
  (concat [1N]
          (->> last-row
               (partition 2 1)
               (map (partial apply +)))
          [1N]))


(def triangle (iterate calc-next-row [1]))

(defn row [n]
  (->> triangle
       (drop (dec n))
       first))



