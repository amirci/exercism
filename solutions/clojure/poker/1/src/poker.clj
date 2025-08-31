(ns poker
  (:require [clojure.string :as st]))


(defn ->nbr [s]
  (read-string
   (st/replace s
               #"[AJQK]"
               {"A" "14" "J" "11" "Q" "12" "K" "13"})))


(defn by-suit [cards]
  (->> cards
       (group-by second)
       (map (fn [[k cards]] [k (sort (map first cards))]))
       (into {})))


(defn by-rank [cards]
  (->> cards
       (map first)
       frequencies))


(defn ->cards [hand]
  (->> hand
       (re-seq #"([\dAJQK]+)([SHDC])")
       (map rest)
       (map (fn [[card suit]] [(->nbr card) suit]))
       ((juxt by-suit by-rank))
       (zipmap [:suits :cards])))

(defn same-suit? [n [_ cards]]
  (= n (count cards)))

(defn in-range? [nbrs]
  (->> nbrs
       (map - (rest nbrs))
       (every? (partial = 1))))


(defn straight-flush [{:keys [suits]}]
  (->> suits
       (filter (partial same-suit? 5))
       first
       second
       (#(when (and % (in-range? %))
           (apply + 900 %)))))


(defn four-of-kind
  "4 cards of the same rank"
  [{:keys [cards]}]
  (let [[[n1 t1] [n2 t2] third] (sort-by second cards)]
    (and (nil? third)
         (= 4 t2)
         (+ 800 (* n2 t2) (/ (* t1 n1) 100.0)))))


(defn full-house
  "3 cards of one rank and 2 cards of another rank"
  [{:keys [cards]}]
  (let [[[n1 t1] [n2 t2] third] (sort-by second cards)]
    (and (nil? third)
         (= 2 t1)
         (+ 700 (* n1 t1) (* n2 t2)))))


(defn sflush [{:keys [suits]}]
  (->> suits
       (filter (partial same-suit? 5))
       first
       second
       (#(when % (apply + 600 %)))))

(def straights?
  (conj
   (set
    (partition 5
               1
               [2 3 4 5 6 7 8 9 10 11 12 13 14]))
   [2 3 4 5 14]))


(defn straight [{:keys [cards]}]
  (when-let [sts (straights? (sort (keys cards)))]
    (let [ks (if (= [2 3 4 5 14] sts) (range 6) sts)]
      (apply + 500 ks))))


(defn three-of-kind
  "3 cards of the same rank"
  [{:keys [cards]}]
  (let [[[n1 t1] [n2 t2] [n3 t3]] (sort-by second > cards)]
    (and (= 3 t1)
         (= 1 t2)
         (+ 400 n1 (/ (+ (* n2 t2) (* n3 t3)) 100.0)))))

(defn two-pair [{:keys [cards]}]
  (let [[[n1 t1] [n2 t2] [n3 t3]] (sort-by second > cards)]
    (and (= 2 t1 t2)
         (+ 300
            n1
            n2
            (/ (* n3 t3) 100.0)))))

(defn one-pair [{:keys [cards]}]
  (let [[[n1 t1]] (sort-by second > cards)]
    (and (= 2 t1)
         (+ 200
            n1))))


(defn high-card [{:keys [cards]}]
  (->> cards
       keys
       sort
       (reduce #(+ %2 (/ %1 10.0)))))


(defn score [hand]
  (some #(% hand)
        [straight-flush
         four-of-kind
         full-house
         sflush
         straight
         three-of-kind
         two-pair
         one-pair
         high-card]))

(defn highest-hands [[[_ h] :as hands]]
  (->> hands
       (filter (comp (partial = h) second))
       (map first)))

(defn best-hands [hands]
  (->> hands
       (map (juxt identity (comp score ->cards)))
       (sort-by second >=)
       highest-hands))


