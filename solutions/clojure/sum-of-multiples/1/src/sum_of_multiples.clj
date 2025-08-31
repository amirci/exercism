(ns sum-of-multiples)


(defn multiples-of [n]
  (iterate (partial + n) n))


(defn sum-of-multiples [nbrs limit]
  (->> nbrs
       (map multiples-of)
       (mapcat (partial take-while (partial > limit)))
       set
       (apply +)))

