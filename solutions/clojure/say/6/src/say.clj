(ns say
  (:require [clojure.string :as s]))

(def until-20
  (clojure.string/split
   "zero one two three four five six seven eight nine
    ten eleven twelve thirteen fourteen fifteen sixteen
    seventeen eighteen nineteen"
   #"\n? +"))

(def decenas
  (clojure.string/split "zero ten twenty thirty forty fifty sixty seventy eighty ninenty" #" "))

(def thousands
  (clojure.string/split "zero thousand million billion" #" "))

(defn split-thousands
  [n]
  (->> n
       (iterate #(quot % 1000))
       (take-while pos?)
       (map #(rem % 1000))))

(defn ensure-range
  [num]
  (when (not (< -1 num 999999999999))
    (throw (IllegalArgumentException. "Not valid range"))))

(defn quot-rem
  [n den]
  [(quot n den) (rem n den)])

(defn combined-number
  [d]
  (let [[q r] (quot-rem d 10)]
    (cond-> (decenas q)
      (pos? r) (str "-" (until-20 r)))))

(defn ->words
  [num]
  (let [[h d] (quot-rem num 100)]
    (cond-> []
      (pos? h)   (conj (until-20 h) "hundred")
      (< 0 d 20) (conj (until-20 d))
      (< 19 d)   (conj (combined-number d))
      :always    (as-> coll (s/join " " coll)))))


(defn add-unit [unit n]
  (when (pos? n)
    (cond-> (->words n)
      (seq unit) (str " " unit))))


(defn number
  [num]
  (ensure-range num)

  (if (zero? num)
    "zero"
    (->> num
         split-thousands
         (map add-unit [nil "thousand" "million" "billion"])
         (filter seq)
         reverse
         (s/join " "))))


