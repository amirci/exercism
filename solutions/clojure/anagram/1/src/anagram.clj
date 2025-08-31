(ns anagram)

(use 'clojure.string)

(defn anagrams-for
  [src words]
  (def src-low (lower-case src))
  (def sorted (sort src-low))
  (defn is-anagram
    [word]
    (def lw (lower-case word))
    (and
      (= (sort lw) sorted)
      (not= lw src-low)))
  (filter is-anagram words))