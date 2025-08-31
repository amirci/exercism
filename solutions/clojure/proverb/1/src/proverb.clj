(ns proverb
  (:require [clojure.string :as st]))

(defn ->verse [[a b]]
  (format "For want of a %s the %s was lost." a b))

(defn final-verse [a]
  (format "And all for the want of a %s." a))

(defn recite [[fst & rst :as words]]
  (cond
    (nil? fst) ""
    (nil? rst) (final-verse fst)
    :else (->> words
               (partition 2 1)
               (mapv ->verse)
               (#(conj %(final-verse fst)))
               (st/join "\n"))))

