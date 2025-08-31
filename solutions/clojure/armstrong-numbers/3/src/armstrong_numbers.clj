(ns armstrong-numbers)

(defn pow
  [x y]
  (reduce * 1 (repeat y x)))

(defn ->digits
  [num]
  (loop [pending num coll ()]
    (if (pos? pending)
      (recur (quot pending 10) (conj coll (rem pending 10)))
      coll)))

(defn armstrong?
  [num]
  (let [digits (->digits num)]
    (->> digits
         (map #(pow % (count digits)))
         (apply +)
         (= num))))

