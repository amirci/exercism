(ns octal)


(def mult-of-8
  (iterate (partial * 8) 1))

(defn to-decimal [s]
  (let [dts (remove neg? (map #(Character/digit % 8) s))]
    (if (= (count dts) (count s))
      (->> dts
           reverse
           (map * mult-of-8)
           (apply +))
      0)))


