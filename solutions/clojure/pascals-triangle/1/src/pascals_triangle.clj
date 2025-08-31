(ns pascals-triangle)


(defn calc-next-row [last-row]
  "right and left of the previous row"
  (->> last-row
       (map bigint)
       (partition 2 1)
       (map (partial apply +))
       (cons 1)
       vec
       (#(conj % 1))))


(defn generate-pascal
  ([] (generate-pascal [1]))
  ([last-row]
   (lazy-seq
    (cons
     last-row
     (generate-pascal (calc-next-row last-row))))))


(def triangle (generate-pascal))

(defn row [n]
  (->> triangle
       (drop (dec n))
       first))



