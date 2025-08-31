(ns strain)

(defn retain [f coll]
  (loop [result [] [fst & rst] coll]
    (cond
      (nil? fst) result
      (f fst) (recur (conj result fst) rst)
      :else (recur result rst))))

(defn discard [f coll]
  (retain (complement f) coll))




