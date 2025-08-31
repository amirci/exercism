(ns queen-attack
  (:require [clojure.string :as s]))

(def empty-board
  (vec (repeat 8 (vec "________"))))


(defn board-string [{:keys [w b]}]
  (cond-> empty-board
    w (assoc-in w \W)
    b (assoc-in b \B)
    :always (->>
             (map (partial interpose \ ))
             (map (partial apply str))
             (interpose "\n")
             (apply str))
    :finally (str "\n")))

(defn slopish [[a b] [c d]]
  [(- d b) (- c a)])

(defn can-attack [{:keys [w b]}]
  (let [[ys xs] (slopish w b)]
    (or (zero? ys)
        (zero? xs)
        (= 1 (/ ys xs)))))


