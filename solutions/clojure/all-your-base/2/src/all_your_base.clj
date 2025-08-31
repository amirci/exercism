(ns all-your-base)

(defn to-decimal [base coll]
  (->> coll
       reverse
       (map * (iterate (partial * base) 1))
       (apply +)))

(defn from-decimal [base num]
  (->> num
       (iterate #(quot % base))
       (take-while pos?)
       (map #(mod % base))
       reverse))


(defn invalid-rng? [base coll]
  (not-every? #(< -1 % base) coll))


(def zeroes? (partial every? zero?))


(defn convert [from-b coll to-b]
  (cond
    (<= from-b 1) nil
    (<= to-b 1) nil
    (empty? coll) []
    (invalid-rng? from-b coll) nil
    (zeroes? coll) [0]
    :else (->> coll
               (to-decimal from-b)
               (from-decimal to-b))))

