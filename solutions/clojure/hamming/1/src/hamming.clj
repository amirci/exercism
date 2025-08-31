(ns hamming)


(defn distance
  [s1 s2]
  (def same-size 
    (or (= (count s1) (count s2)) nil))
  (defn not-same [[a b]] (not= a b))
  (and same-size
       (count
         (filter not-same (map vector s1 s2)))))