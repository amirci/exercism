(ns binary-search)

(defn middle
  ([numbers] (middle 0 (- (count numbers) 1)))
  ([low high] (quot (+ low high) 2)))


;(defn ^:dynamic search-for
(defn search-for
  ([n numbers] (search-for n numbers 0 (- (count numbers) 1)))
  ([n numbers low high]
   (let [m (middle low high)
         m-val (nth numbers m)]
        (cond
          (> low high) (throw (Exception. "#not found"))
          (= n m-val) m
          (> n m-val) (search-for n numbers (+ m 1) high)
          (< n m-val) (search-for n numbers low (- m 1))))))