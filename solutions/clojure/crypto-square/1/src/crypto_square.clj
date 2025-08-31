(ns crypto-square
  (:require [clojure.string :as st]))

(defn normalize-plaintext [s]
  (-> s
      st/lower-case
      (st/replace #"[^a-z\d]" "")))


(defn square-size
  "c = r + 1 or c = r"
  [s]
  (-> s
      count
      Math/sqrt
      Math/ceil
      int))


(defn plaintext-segments [s]
  (let [pt (normalize-plaintext s)
        size (square-size pt)]
    (->> pt
         (partition-all size)
         (mapv (partial apply str)))))

(defn pad-if-needed
  [w n]
  (format (str "%-" n "s") w))

(defn update-last-segment [segs]
  (update segs
          (dec (count segs))
          pad-if-needed (count (first segs))))

(defn cipher-segs [s]
  (->> s
       plaintext-segments
       update-last-segment
       (apply map str)))

(defn ciphertext [s]
  (->> s
       cipher-segs
       (map st/trim)
       st/join))


(defn normalize-ciphertext [s]
  (->> s
       cipher-segs
       (st/join " ")))

