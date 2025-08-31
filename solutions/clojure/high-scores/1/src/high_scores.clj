(ns high-scores)

(def scores identity)

(def latest last)

(def personal-best (partial apply max))

(defn personal-top-three
  "Returns the top three scores"
  [scores]
  (->> scores
       (sort >)
       (take 3)))
