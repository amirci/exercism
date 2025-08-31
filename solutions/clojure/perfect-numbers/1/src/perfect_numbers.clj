(ns perfect-numbers)

(defn divisor [n div]
  (if (zero? (rem n div))
    div
    0))

(defn classify [n]
  (when (neg? n) (throw (IllegalArgumentException.)))
  (->> (quot n 2)
       inc
       (range 2)
       (map (partial divisor n))
       (apply + 1)
       (compare n)
       ({1 :deficient 0 :perfect -1 :abundant})))

