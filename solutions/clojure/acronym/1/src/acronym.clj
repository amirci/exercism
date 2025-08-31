(ns acronym
  (:require [clojure.string :as s]))

(defn split-words [ws]
  (->> ws
       (drop 1)
       (apply str)
       (re-seq #"[A-Z][a-z]+")
       (concat [(subs ws 0 1)])))

(defn acronym [s]
  (->> s
       (re-seq #"\w+")
       (mapcat split-words)
       (map first)
       (apply str)
       s/upper-case))

