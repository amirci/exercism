(ns pangram)

(defn pangram? [s]
  (->> s
       clojure.string/lower-case
       (filter #(Character/isLetter %))
       distinct
       count
       (= 26)))
