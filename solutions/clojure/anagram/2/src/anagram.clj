(ns anagram)

(use '[clojure.string :as str])

(defn anagrams-for
  [src words]
  (def src-low (str/lower-case src))
  (def sorted (sort src-low))
  (defn is-anagram
    [word]
    (let [lw (str/lower-case word)]
      (and
        (= (sort lw) sorted)
        (not= lw src-low))))
  (filter is-anagram words))