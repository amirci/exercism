(ns pig-latin
  (:require [clojure.string :as st]))

;Rule 1: If a word begins with a vowel sound, add an "ay" sound to the end of the word. Please note that "xr" and "yt" at the beginning of a word make vowel sounds (e.g. "xray" -> "xrayay", "yttria" -> "yttriaay").
;Rule 2: If a word begins with a consonant sound, move it to the end of the word and then add an "ay" sound to the end of the word. Consonant sounds can be made up of multiple consonants, a.k.a. a consonant cluster (e.g. "chair" -> "airchay").
;Rule 3: If a word starts with a consonant sound followed by "qu", move it to the end of the word, and then add an "ay" sound to the end of the word (e.g. "square" -> "aresquay").
;Rule 4: If a word contains a "y" after a consonant cluster or as the second letter in a two letter word it makes a vowel sound (e.g. "rhythm" -> "ythmrhay", "my" -> "ymay").

(def vowel? (set "aeiou"))

(def consonant? (complement vowel?))

(def ny-cons?
  (every-pred consonant? (partial not= \y)))


(defn vowely? [s]
  (->> ["xr" "xt" "yt"]
       (some (partial st/starts-with? s))))


(defn y-after-cc? [cc rst]
  (and (empty? cc) (= \y (first rst))))


(defn qu-after-cc? [cc rst]
  (and (= \q (last cc)) (= \u (first rst))))


(defn split-cons [s]
  (let [[cc rst] (split-with ny-cons? s)]
    (cond
      (vowely? s) [s]
      (y-after-cc? cc rst) [(rest rst) "y"]
      (qu-after-cc? cc rst) [(rest rst) cc "u"]
      :else [rst cc])))


(defn ->pig [s]
  (->> "ay"
       (conj (split-cons s))
       flatten
       (apply str)))


(defn translate [phrase]
  (->> phrase
       (re-seq #"\w+")
       (map ->pig)
       (st/join " ")))
