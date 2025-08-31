(ns nth-prime)

(defn prime? [primes n]
  (not-any? #(zero? (mod n %)) primes))

(def not-prime? (complement prime?))

(defn next-prime [[fst :as primes]]
  (->> (iterate (partial + 2) fst)
       (drop 1)
       (drop-while (partial not-prime? primes))
       first
       (#(cons % primes))))

(def primes
  (concat [2 3 5]
          (map first
               (iterate next-prime [7 3 5 2]))))

(defn nth-prime [n]
  (when-not (pos? n) (throw (IllegalArgumentException.)))
  (nth primes (dec n)))

