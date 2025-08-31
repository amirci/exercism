(ns meetup
  (:require [clj-time.core :as t]))

(def days [:monday :tuesday :wednesday :thursday :friday :saturday :sunday])

(defn- find-first 
  [dow [y m d]]
  (let [current (t/day-of-week (t/date-time y m d))
        expected (->> dow (.indexOf days) (inc))
        diff (- expected current)
        advance (mod diff 7)]
    [y m (+ advance d)]))

(defn- add-weeks
  [n [y m d]]
  [y m (+ d (* n 7))])

(defn- find-last 
  [[y m d]]
  (let [week4 (t/plus (t/date-time y m d) (t/weeks 4))
        week3 (t/plus (t/date-time y m d) (t/weeks 3))]
       (if (= m (t/month week4)) 
           [y m (t/day week4)] 
           [y m (t/day week3)])))

(defn meetup
  [m y dow adjust]
  (cond
    (= adjust :teenth) (->> [y m 13] (find-first dow))
    (= adjust :first ) (->> [y m  1] (find-first dow))
    (= adjust :second) (->> [y m  1] (find-first dow) (add-weeks 1))
    (= adjust :third ) (->> [y m  1] (find-first dow) (add-weeks 2))
    (= adjust :fourth) (->> [y m  1] (find-first dow) (add-weeks 3))
    (= adjust :last  ) (->> [y m  1] (find-first dow) find-last)
    :else [y m 1]))