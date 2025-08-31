(ns isbn-verifier
  (:require [clojure.string :as str]))

;(x1 * 10 + x2 * 9 + x3 * 8 + x4 * 7 + x5 * 6 + x6 * 5 + x7 * 4 + x8 * 3 + x9 * 2 + x10 * 1) mod 11 == 0

(defn digit->int
  [c]
  (cond-> c
    (char? c) (Character/digit 10)))


(defn valid?
  [parsed]
  (->> parsed
       (replace {\X 10})
       (map digit->int)
       (map * (range 10 0 -1))
       (apply +)
       (#(mod % 11))
       zero?))


(def isbn-regex #"(\d)-?(\d{3})-?(\d{5})-?([\dX])")

(defn isbn?
  [isbn]
  (true?
   (when-let [parsed (re-matches isbn-regex isbn)] 
     (->> parsed
          (drop 1)
          (apply str)
          valid?))))

