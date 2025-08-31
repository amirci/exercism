(ns kindergarten-garden
  (:require [clojure.string :as st]))

(def ->plants
  {\R :radishes
   \C :clover
   \G :grass
   \V :violets})

(def children
  ["alice" "bob" "charlie" "david" "eve"
   "fred" "ginny" "harriet" "ileana"
   "joseph" "kincaid" "larry"])

(def map-plants (partial map ->plants))

(def ->name (comp keyword st/lower-case))

(defn garden
  ([plants] (garden plants children))
  ([plants students]
   (let [children (sort (map ->name students))]
     (->> plants
          st/split-lines
          (map (partial partition 2))
          (apply map (comp map-plants flatten vector))
          (zipmap children)))))



