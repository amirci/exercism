(ns etl)

(defn expand-pairs
  [[k vs]]
  (map #(vector (clojure.string/lower-case %) k) vs))

(defn transform
  [values]
  (->> values
       (mapcat expand-pairs)
       (into {})))

