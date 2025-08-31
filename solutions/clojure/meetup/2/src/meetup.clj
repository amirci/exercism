(ns meetup
  (:require
   [java-time :as tm]))

(def adjustments? #{:teenth :first :second :third :fourth :last})

(def snd->weeks {:second 1 :third 2 :fourth 3})

(def second? snd->weeks)

(def last? (partial = :last))

(def teenth? (partial = :teenth))

(def days [:monday :tuesday :wednesday :thursday :friday :saturday :sunday])

(defn ->dow-idx
  [dow]
  (->> dow (.indexOf days) (inc)))

(defn- find-first 
  [date dow]
  (let [current (.getValue (tm/day-of-week date))
        advance (-> dow
                    ->dow-idx
                    (- current)
                    (mod 7))]
    (tm/plus date (tm/days advance))))

(defn- find-last 
  [date dow]
  (-> date
      tm/year-month
      .atEndOfMonth
      (tm/minus (tm/days 6))
      (find-first dow)))

(defn ->vec
  [date]
  (tm/as date :year :month-of-year :day-of-month))

(defn meetup
  [m y dow adjust]
  (cond-> (tm/local-date y m 1)
    (teenth?      adjust) (tm/plus (tm/days 12))
    (adjustments? adjust) (find-first dow)
    (second?      adjust) (tm/plus (tm/weeks (snd->weeks adjust)))
    (last?        adjust) (find-last dow)
    :always               ->vec))


