(ns sum-of-multiples)


(defn sum-of-multiples [nbrs limit]
  (->> nbrs
       (mapcat #(range % limit %))
       set
       (apply +)))

