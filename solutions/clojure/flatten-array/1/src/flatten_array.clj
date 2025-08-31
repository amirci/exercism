(ns flatten-array)

(defn flatten [arr]
  (loop [[fst & rst :as coll] arr
         pending ()
         result  []]
    (cond
      (empty? coll) (if (empty? pending)
                      result
                      (recur (first pending) (rest pending) result))
      (nil? fst)     (recur rst pending result)
      (coll? fst)    (recur fst (conj pending rst) result)
      :else          (recur rst pending (conj result fst)))))



