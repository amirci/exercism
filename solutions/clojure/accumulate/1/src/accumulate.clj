(ns accumulate)

(defn accumulate [f coll] 
  (loop [[fst & rst] coll result []]
    (if-not fst
      result
      (recur rst (conj result (f fst))))))
