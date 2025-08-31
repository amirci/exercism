(ns binary-search)

(defn middle
  ([numbers] (middle 0 (- (count numbers) 1)))
  ([low high] (quot (+ low high) 2)))

(defn search-for
  ([n numbers] (search-for n numbers 0 (- (count numbers) 1)))
  ([n numbers low high]
   (let [m (middle low high)
         m-val (nth numbers m)]
     (cond
       (< high low) (throw (Exception. "#not found"))
       (= n m-val)  m
       (< m-val n)  (recur n numbers (inc m) high)
       (< n m-val)  (recur n numbers low (dec m))))))
