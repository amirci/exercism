(ns armstrong-numbers)

(defn pow
  [x y]
  (reduce * 1 (repeat y x)))

(defn armstrong?
  [num]
  (let [digits (str num)]
    (->> digits
         (map #(Character/digit % 10))
         (map #(pow % (count digits)))
         (apply +)
         (= num))))

