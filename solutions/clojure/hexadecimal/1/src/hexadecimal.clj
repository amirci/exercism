(ns hexadecimal)

(def mult-of-16
  (iterate (partial * 16) 1))

(defn hex-to-int [s]
  (let [dts (remove neg? (map #(Character/digit % 16) s))]
    (if (= (count dts) (count s))
      (->> dts
           reverse
           (map * mult-of-16)
           (apply +))
      0)))
