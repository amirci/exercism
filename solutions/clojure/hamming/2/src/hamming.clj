(ns hamming)

(defn same-size?
  [s1 s2]
  (->> [s1 s2]
       (map count)
       (apply =)))

(defn count-diff
  [s1 s2]
  (->> (map not= s1 s2)
       (filter true?)
       count))

(defn distance
  [s1 s2]
  (or
   (and (same-size? s1 s2)
        (count-diff s1 s2))
   nil))
