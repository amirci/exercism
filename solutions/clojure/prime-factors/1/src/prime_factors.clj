(ns prime-factors)

(defn divides? [m n]
  (zero? (mod m n)))


(defn mk-factor [n f] [(quot n f) f])


(defn find-factor [[n]]
  (cond
    (= n 1) [1 1]
    (divides? n 2) (mk-factor n 2)
    :else (->> (range 3 (inc n) 2)
               (filter (partial divides? n))
               first
               (mk-factor n))))


(defn of [n]
  (->> [n]
       (iterate find-factor)
       (take-while (partial not= [1 1]))
       (drop 1)
       (map last)))

