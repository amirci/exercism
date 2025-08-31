(ns nth-prime)

(defn multiple? [n div]
  (zero? (mod n div)))

(defn prime? [n]
  (not-any? (partial multiple? n)
            (range 3 (inc (Math/sqrt n)))))

(def primes
  (concat [2 3 5]
          (filter prime? (iterate (partial + 2) 7))))


(defn nth-prime [n]
  (when-not (pos? n) (throw (IllegalArgumentException.)))
  (nth primes (dec n)))

