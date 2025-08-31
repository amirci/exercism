(ns matrix
  (:require [clojure.string :as s]))

(defn ->matrix [s]
  (->> s
       (s/split-lines)
       (mapv (comp read-string (partial format "[%s]")))))

(defn get-row
  "Returns the i-th row of the matrix s"
  [s i]
  (-> s
     ->matrix
     (get (dec i))))


(defn get-column
  "Returns the i-th column of the matrix s"
  [s i]
  (->> s
       ->matrix
       (apply mapv vector)
       (#(get % (dec i)))))

