(ns transpose
  (:require [clojure.string :as str]))

(defn- max-length [coll]
  (->> coll
       (map count)
       (apply max)))

(defn- add-spaces [lines length]
  (if (pos? length)
    (let [padding (format "%%-%ds" length)]
      (for [line lines] (format padding line)))
    lines))

(def ^:private spaces-at-the-end #"\ +$")

(defn- remove-right-padding [s]
  (str/replace s spaces-at-the-end ""))

(defn- reduce-padding [diff spaces]
  (apply str (drop diff spaces)))

(defn- adjust-padding-based-on-next [coll [prev after]]
  (let [diff (- (count (coll prev)) (count (coll after)))]
    (update coll prev str/replace spaces-at-the-end (partial reduce-padding diff))))


(defn- adjust-right-padding [coll]
  (if (empty? coll)
    coll
    (let [coll (update coll (dec (count coll)) remove-right-padding)]
      (->> (range (count coll))
           (partition 2 1)
           (reverse)
           (reduce adjust-padding-based-on-next coll)))))


(defn transpose
  "Returns the transposed version of the given string."
  [s]
  (let [lines (str/split-lines s)]
    (->> lines
         max-length
         (add-spaces lines)
         (apply map vector)
         (map #(apply str %))
         vec
         (adjust-right-padding)
         (str/join "\n"))))

