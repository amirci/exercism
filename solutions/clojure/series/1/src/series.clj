(ns series)

(defn slices [string length]
  (if (zero? length)
    [""]
    (->> string
         (partition length 1)
         (map (partial apply str)))))
