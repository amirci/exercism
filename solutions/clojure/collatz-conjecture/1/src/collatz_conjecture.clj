(ns collatz-conjecture)

(defn collatz-num 
  [n]
  (if (even? n) 
    (/ n 2)
    (-> n (* 3) (+ 1))))

(defn collatz 
  [n]
  (when (<= n 0) (throw (IllegalArgumentException.)))
  (->> n
       (iterate collatz-num)
       (take-while (partial not= 1))
       count))