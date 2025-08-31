(ns matching-brackets)

(def bracket? #(.contains "[]{}()" (str %)))

(defn open?
  [br]
  (#{\[\{\(} br))

(defn pair?
  [br1 br2]
  (#{[\[\]] [\{\}] [\(\)]} [br1 br2]))

(defn valid?
  [brackets]
  (->> brackets
       (filter bracket?)
       (reduce
         (fn [[top :as stack] br]
           (cond
             (open? br)     (conj stack br)
             (pair? top br) (pop stack)
             :else          (reduced [:invalid])))
         '())
       empty?))

