(ns armstrong-numbers)

(defn pow
  [x y]
  (if (zero? y)
    1
    (reduce * x (take (dec y) (repeat x)))))

(defn armstrong?
  [num]
  (let [digits (str num)]
    (->> digits
         (map #(- (int %) (int \0)))
         (map #(pow % (count digits)))
         (apply +)
         (= num))))

