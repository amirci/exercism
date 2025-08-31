(ns collatz-conjecture)

(defn collatz-num 
  [n]
  (if (even? n) 
    (/ n 2)
    (-> n (* 3) inc)))

(defn collatz 
  [n]
  (assert (pos? n) "Invalid arg, it needs a positive number")
  (->> n
       (iterate collatz-num)
       (take-while (partial not= 1))
       count))
