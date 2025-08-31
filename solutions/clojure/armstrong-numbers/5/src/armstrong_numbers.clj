(ns armstrong-numbers)

(defn pow
  [y x]
  (apply * (repeat y x)))

(defn ->digits
  [num]
  (->> num
       (iterate #(quot % 10))
       (take-while pos?)
       (map #(rem % 10))))

(defn armstrong?
  [num]
  (let [digits (->digits num)]
    (->> digits
         (map (partial pow (count digits)))
         (apply +)
         (= num))))
