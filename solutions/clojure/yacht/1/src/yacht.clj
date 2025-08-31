(ns yacht
  (:require [clojure.string :as str]))

(defmulti dice-score (fn [arg1 _] arg1))

(defmethod dice-score :yacht [_ freq]
  (when (= 1 (count freq)) 50))

(defn- sum-dice [freq]
  (->> freq
       (map (partial apply *))
       (apply +)))

(defn- with-count-sorted [freq]
  (let [[[n1 q1] [n2 q2]] (sort-by second freq)]
    [(count freq) n1 q1 n2 q2]))

(defmethod dice-score :full-house [_ freq]
  (let [[n _ q1 _ q2] (with-count-sorted freq)]
    (and (= 2 n)
         (= 2 q1)
         (= 3 q2)
         (sum-dice freq))))

(defmethod dice-score :four-of-a-kind [_ freq]
  (let [[total n1 q1 n2 q2] (with-count-sorted freq)
        n (or n2 n1)]
    (and (<= 1 total 2)
         (or (= 5 q1) (= 4 q2))
         (* n 4))))


(defn- set-check
  [expected freq]
  (when (= expected (set (keys freq))) 30))

(defmethod dice-score :little-straight [_ freq]
  (set-check #{1 2 3 4 5} freq))


(defmethod dice-score :big-straight [_ freq]
  (set-check #{2 3 4 5 6} freq))


(defmethod dice-score :choice [_ freq]
  (sum-dice freq))

(def ->dice
  {:ones 1 :twos 2 :threes 3 :fours 4 :fives 5 :sixes 6})

(defmethod dice-score :default [k freq]
  (let [n (->dice k)]
    (* n (freq n 0))))

(defn ->kebab [s]
  (keyword (str/replace s " " "-")))

(defn score [dice category]
  (or
   (dice-score (->kebab category) (frequencies dice))
   0))

