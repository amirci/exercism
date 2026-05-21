(ns food-chain
  (:require [clojure.string :as str]))

(def animals [{:name "fly"}
              {:name "spider"
               :rhyme "It wriggled and jiggled and tickled inside her."
               :description "that wriggled and jiggled and tickled inside her"}
              {:name "bird" :rhyme "How absurd to swallow a bird!"}
              {:name "cat" :rhyme "Imagine that, to swallow a cat!"}
              {:name "dog" :rhyme "What a hog, to swallow a dog!"}
              {:name "goat" :rhyme "Just opened her throat and swallowed a goat!"}
              {:name "cow" :rhyme "I don't know how she swallowed a cow!"}
              {:name "horse" :rhyme "She's dead, of course!"}])

(def perhaps-die "I don't know why she swallowed the fly. Perhaps she'll die.")

(defn- old-lady-verse [animal]
  (format "I know an old lady who swallowed a %s." (animal :name)))

(defn- join-into-lines
  ([coll] (join-into-lines "\n" coll))
  ([sep coll] (str/join sep coll)))

(def double-sep "\n\n")

(defn- add-second-verse-rhyme [verses {:keys [rhyme]}]
  (cond-> verses
    rhyme (conj rhyme)))

(def why-did-she-swallowed (vec (partition 2 1 animals)))

(defn- she-swallowed-verse [n]
  (let [[catch swallowed] (get why-did-she-swallowed n)
        opt-description (if (:description catch) (str " " (:description catch)) "")]
    (format "She swallowed the %s to catch the %s%s." (:name swallowed) (:name catch) opt-description)))

(defn- add-previous-animals [verses start]
  (->> (range start)
       (map she-swallowed-verse)
       (into verses)))

(defn- not-horse-verses [verses start animal]
  (cond-> verses
    (not= "horse" (:name animal)) (-> (conj perhaps-die) (add-previous-animals start))))

(defn- verse [start]
  (let [animal (animals start)]
    (-> '()
        (not-horse-verses start animal)
        (add-second-verse-rhyme animal)
        (conj (old-lady-verse animal))
        join-into-lines)))

(defn recite
  "Returns the lyrics of the song: 'I Know an Old Lady Who Swallowed a Fly.'"
  [start-verse end-verse]
  (->> (range (dec start-verse) end-verse)
       (map verse)
       (join-into-lines double-sep)))


