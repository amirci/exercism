(ns flatten-array)

(defn flatten [arr]
  (loop [[fst & rst :as coll] arr
         result  []]
    (cond
      (empty? coll) result
      (coll? fst)   (recur (concat fst rst) result)
      (not (nil? fst)) (recur rst (conj result fst))
      :else         (recur rst result))))

(flatten [0 2 [[2 3] 8 100 4 [[[50]]]] -2])



