(ns scrabble-score)

(def reverse-scores
  {1 "AEIOULNRST"
   2 "DG"
   3 "BCMP"
   4 "FHVWY"
   5 "K"
   8 "JX"
   10 "QZ"})

(def scores
  (->> (for [[score ltrs] reverse-scores l ltrs] [l score])
       (into {})))


(def score-letter (comp scores first clojure.string/upper-case))

(defn score-word [word]
  (->> word
       (map score-letter)
       (apply +)))
