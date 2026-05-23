(ns resistor-color-trio)

(def color->value
  {"black" 0
   "brown" 1
   "red" 2
   "orange" 3
   "yellow" 4
   "green" 5
   "blue" 6
   "violet" 7
   "grey" 8
   "white" 9})

(def units [[1000000000 "gigaohms"] [1000000 "megaohms"] [1000 "kiloohms"] [1 "ohms"]])

(defn- exp-10 [n]
  (apply * (repeat n 10)))


(defn- add-units [n]
  (if (zero? n)
    "0 ohms"
    (->> units
         (filter #(<= (first %) n))
         first
         ((fn [[exp unit]] (str (quot n exp) " " unit))))))


(defn resistor-label
  "Returns the resistor label based on the given color bands."
  [colors]
  (let [[a b zeroes] (map color->value colors)]
    (->> a
     (* 10)
     (+ b)
     (* (exp-10 zeroes))
     (add-units))))
