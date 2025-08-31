(ns say
  (:require [clojure.string :as s :refer [join]]))

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

(defn quot-rem
  [n den]
  [(quot n den) (rem n den)])

(defn split-thousands
  [n]
  (->> [n 0]
       (iterate #(quot-rem (first %) 1000))
       (take-while #(not= % [0 0]))
       (drop 1)
       (map second)))

(defn ensure-range
  [num]
  (when (not (< -1 num 999999999999))
    (throw (IllegalArgumentException. "Not valid range"))))

(defn combined-number
  [coll d]
  (let [even? (zero? (rem d 10))]
    (cond-> (decenas (quot d 10))
      (not even?) (str "-" (until-20 (rem d 10)))
      :always     (as-> s (conj coll s)))))

(defn ->words
  [num]
  (let [[h d] (quot-rem num 100)]
    (cond-> []
      (pos? h)   (conj (until-20 h) "hundred")
      (< 0 d 20) (conj (until-20 d))
      (< 19 d)   (combined-number d)
      :always (as-> coll (clojure.string/join " " coll)))))

(defn add-thousands
  [coll]
  (->> coll
       (map vector [nil "thousand" "million" "billion"])
       (filter (comp seq second))
       flatten
       reverse
       (filter seq)))

(defn number
  [num]
  (ensure-range num)
  (if (zero? num)
    "zero"
    (->> (split-thousands num)
         (map ->words)
         add-thousands
         (clojure.string/join " "))))

