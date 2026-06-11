(ns transpose
  (:require [clojure.string :as str]))

(defn- max-length [coll]
  (->> coll
       (map count)
       (apply max)))

(defn- format-width [length]
  (format "%%-%ds" length))

(defn- pad-lines-same-length [lines length]
  (if (pos? length)
    (let [padding (format-width length)]
      (for [line lines] (format padding line)))
    lines))

(def ^:private spaces-at-the-end #"\ +$")

(defn- remove-right-padding [s]
  (str/replace s spaces-at-the-end ""))


(defn- pad-right [s length]
  (if (zero? length)
    s
    (format (format-width length) s)))


(defn- change-padding [s length]
  (-> s
      remove-right-padding
      (pad-right length)))


(defn- adjust-right-padding-to [{:keys [max-length] :as acc} s]
  (let [padded (change-padding s max-length)]
    (-> acc
        (update :result conj padded)
        (assoc :max-length (max max-length (count padded))))))


(defn- adjust-right-padding [coll]
  (if (empty? coll)
    coll
    (->> coll
         reverse
         (reduce adjust-right-padding-to {:max-length 0 :result (list)})
         :result)))


(defn transpose
  "Returns the transposed version of the given string."
  [s]
  (let [lines (str/split-lines s)]
    (->> lines
         max-length
         (pad-lines-same-length lines)
         (apply mapv vector)
         (mapv #(apply str %))
         (adjust-right-padding)
         (str/join "\n"))))

