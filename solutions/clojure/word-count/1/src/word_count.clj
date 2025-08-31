(ns word-count)


(defn word-count
  [phrase]
  (def words
    (re-seq #"\w+"
      (clojure.string/lower-case phrase)))

  (frequencies words)
)