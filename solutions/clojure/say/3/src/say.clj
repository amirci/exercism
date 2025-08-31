(ns say
  (:require [clojure.string :as str]))

(def until-20
  (clojure.string/split
   "zero one two three four five six seven eight nine
    ten eleven twelve thirteen fourteen fifteen sixteen
    seventeen eighteen nineteen"
   #"\n? +"))

(def decenas
  (clojure.string/split
   "zero ten twenty thirty forty fifty sixty seventy eighty ninenty"
   #" "))

(def thousands
  (clojure.string/split
   "zero thousand million billion" #" "))

(defn quot-rem
  [n den]
  [(quot n den) (rem n den)])

(defn split-thousands
  [n]
  (->> [n 0]
       (iterate #(quot-rem (first %) 1000))
       (take-while #(not= % [0 0]))
       (drop 1)
       (map second)
       (map-indexed vector)))

(defn assert-arg
  [num pred msg]
  (when (pred num)
    (throw (IllegalArgumentException. msg))))

(defn ensure-range
  [num]
  (assert-arg num neg? "Not valid negative number")
  (assert-arg num (partial < 999999999999) "Not valid for above 999999999999"))

(defn ->words
  [[idx num]]
  (let [[h d] (quot-rem num 100)
        even? (zero? (rem d 10))]
    (cond-> ""
      (pos? h)   (str (get until-20 h) " hundred")
      (< 0 d 20) (str " " (get until-20 d))
      (< 19 d)   (str " " (get decenas (quot d 10)))
      (and
       (< 19 d)
       (not even?)) (str "-" (get until-20 (rem d 10)))
      (and
       (pos? num)
       (pos? idx))  (str " " (get thousands idx))
      :always clojure.string/trim)))

(defn number
  [num]
  (ensure-range num)
  (if (zero? num)
    "zero"
    (->> (split-thousands num)
         (map ->words)
         reverse
         (clojure.string/join " ")
         clojure.string/trim)))

