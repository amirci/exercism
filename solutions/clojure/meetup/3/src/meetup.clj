(ns meetup
  (:import [java.time LocalDate YearMonth]))

(def adjustments?
  #{:teenth :first :second :third :fourth :last})

(def adj->weeks {:second 1 :third 2 :fourth 3})

(def second? adj->weeks)

(def last? (partial = :last))

(def teenth? (partial = :teenth))

(def days [:monday :tuesday :wednesday :thursday :friday :saturday :sunday])

(defn ->dow-idx
  [dow]
  (->> dow (.indexOf days) (inc)))

(defn- find-first
  [date dow]
  (let [current (-> date .getDayOfWeek .getValue)
        advance (-> dow
                    ->dow-idx
                    (- current)
                    (mod 7))]
    (.plusDays date advance)))


(defn- find-last
  [date dow]
  (-> date
      YearMonth/from
      .atEndOfMonth
      (.minusDays 6)
      (find-first dow)))


(defn ->vec
  [date]
  [(.getYear date)
   (.getMonthValue date)
   (.getDayOfMonth date)])

(->vec (LocalDate/now))
(defn meetup
  [m y dow adj]
  (cond-> (LocalDate/of y m 1)
    (teenth?      adj) (.plusDays 12)
    (adjustments? adj) (find-first dow)
    (second?      adj) (.plusWeeks (adj->weeks adj))
    (last?        adj) (find-last dow)
    :always            ->vec))


