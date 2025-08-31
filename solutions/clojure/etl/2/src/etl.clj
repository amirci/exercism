(ns etl)

(defn transform
  [values]
  (reduce 
    (fn [hsh [k words]] 
        (reduce #(assoc %1 (.toLowerCase %2) k) hsh words))
    {} 
    values))