(ns luhn
  (:require [clojure.string :as st]))

(defn adjust [n]
  (cond-> n
    (< 9 n) (- 9)))


(defn double-digit [idx n]
  (cond-> n
    (odd? idx) (-> (* 2) adjust)))


(defn valid? [s]
  (let [s (st/replace s #"\s" "")]
    (and (< 1 (count s))
         (->> s
              (map #(Character/digit % 10))
              reverse
              (map-indexed double-digit)
              (apply +)
              (#(mod % 10))
              zero?))))


