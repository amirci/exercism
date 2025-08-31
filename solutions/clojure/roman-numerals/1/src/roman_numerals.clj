(ns roman-numerals)

(defn ->digits [n]
  (->> n
       (iterate #(quot % 10))
       (take-while pos?)
       (map #(rem % 10))))


(defn roman-chars [l m u ]
  [[] [l] [l l] [l l l] [l m] [m] [m l] [m l l] [m l l l] [l u]])


(def triplets
  (->> "IVXXLCCDMMQP"
       (partition 3)
       (map (partial apply roman-chars))
       vec))


(defn ->roman [i d]
  (reverse
   (get-in triplets [i d])))


(defn numerals [num]
  (->> num
       ->digits
       (map-indexed ->roman)
       (apply concat)
       reverse
       (apply str)))


