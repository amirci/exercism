(ns eliuds-eggs)

(defn egg-count
  "Returns the number of 1 bits in the binary representation of the given number."
  [number]
  (->> number
       (iterate #(bit-shift-right % 1))
       (take-while pos?)
       (filter odd?)
       count))

(egg-count 5)
(bit-shift-right 5 1)
