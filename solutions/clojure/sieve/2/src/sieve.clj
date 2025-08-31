(ns sieve)

(defn multiple-of? [n m]
  (->> m
       (iterate #(- % n))
       (drop-while pos?)
       first
       zero?))

(def not-multiple-of? (complement multiple-of?))

(defn sieve-step
  [primes [fst & rst]]
  (if fst
    (recur (conj primes fst)
           (filter (partial not-multiple-of fst) rst))
    primes))

(defn sieve
  [n]
  (sieve-step [] (range 2 (inc n))))

