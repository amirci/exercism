(ns isogram)

(defn isogram? [word]
  (->> word
       clojure.string/lower-case
       (filter #(Character/isLetter %))
       frequencies
       (filter (comp (partial < 1) second))
       empty?))
