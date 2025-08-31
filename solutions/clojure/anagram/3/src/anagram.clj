(ns anagram
  (:require [clojure.string :as str]))

(defn anagram?
  [original sorted word]
  (let [lw (str/lower-case word)]
    (and
     (= (sort lw) sorted)
     (not= lw original))))

(defn anagrams-for
  [src words]
  (let [src-low (str/lower-case src)
        sorted (sort src-low)]
    (filter (partial anagram? src-low sorted) words)))
