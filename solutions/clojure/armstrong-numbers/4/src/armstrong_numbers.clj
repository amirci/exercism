(ns armstrong-numbers)

(defn pow
  [y x]
  (apply * (repeat y x)))

(def qr10 (juxt #(quot % 10) #(rem % 10)))

(defn ->digits
  [num]
  (loop [[q r :as pending] (qr10 num) coll ()]
    (if (= [0 0] pending)
      coll
      (recur (qr10 q) (conj coll r)))))

(defn armstrong?
  [num]
  (let [digits (->digits num)]
    (->> digits
         (map (partial pow (count digits)))
         (apply +)
         (= num))))
