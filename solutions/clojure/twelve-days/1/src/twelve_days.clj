(ns twelve-days
  (:require [clojure.string :as st]))

(def gifts
  (vec
   (reverse
    ["a Partridge in a Pear Tree"
     "two Turtle Doves"
     "three French Hens"
     "four Calling Birds"
     "five Gold Rings"
     "six Geese-a-Laying"
     "seven Swans-a-Swimming"
     "eight Maids-a-Milking"
     "nine Ladies Dancing"
     "ten Lords-a-Leaping"
     "eleven Pipers Piping"
     "twelve Drummers Drumming"])))


(def days
  ["first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"])

(defn the-x-day-of-christmas [n]
  (format "On the %s day of Christmas my true love gave to me: " (days n)))


(def partridge-pear-tree (gifts 11))

(defn- verses [start-verse]
  (if (= 1 start-verse)
    partridge-pear-tree
    (let [start (- 12 start-verse)
          end (dec (+ start start-verse))]
      (->> partridge-pear-tree
           (format "and %s")
           (conj (subvec gifts start end))
           (st/join ", ")))))


(defn verse [start-verse]
  (apply str
         (the-x-day-of-christmas (dec start-verse))
         (verses start-verse)
         "."))


(defn recite
  "Returns the lyrics of the song: 'The Twelve Days of Christmas.'"
  [start-verse end-verse]
  (->> (inc end-verse)
       (range start-verse)
       (map verse)
       (st/join "\n")))
