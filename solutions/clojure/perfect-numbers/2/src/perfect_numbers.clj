(ns perfect-numbers)

(defn classify [n]
  (when (neg? n) (throw (IllegalArgumentException.)))

  (->> (quot n 2)
       inc
       (range 2)
       (filter #(zero? (rem n %)))
       (apply + 1)
       (compare n)
       ({1 :deficient 0 :perfect -1 :abundant})))

