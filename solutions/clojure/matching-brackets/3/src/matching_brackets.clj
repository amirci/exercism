(ns matching-brackets)

(def brackets?
  {\[ \]
   \{ \}
   \( \)})

(def bracket? #(.contains "[]{}()" (str %)))

(def open? brackets?)

(defn pair?
  [br1 br2]
  (= (brackets? br1) br2))

(defn match-brackets
  [[top :as stack] br]
  (cond
    (open? br)     (conj stack br)
    (pair? top br) (pop stack)
    :else          (reduced [:invalid])))

(defn valid?
  [brackets]
  (->> brackets
       (filter bracket?)
       (reduce match-brackets ())
       empty?))

