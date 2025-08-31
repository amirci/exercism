(ns space-age)

(def a-day (* 24 (* 60 60)))

(def earth-year (* 365.25 a-day))

(defn on-earth
  [seconds]
  (/ seconds earth-year))

(defn on-mercury
  [seconds]
  (/ seconds (* 87.97 a-day)))

(defn on-venus
  [seconds]
  (/ seconds (* 224.7 a-day)))

(defn on-mars
  [seconds]
  (/ seconds (* 686.98 a-day)))

(defn on-jupiter
  [seconds]
  (/ seconds (* 11.86 earth-year)))

(defn on-saturn
  [seconds]
  (/ seconds (* 29.4 earth-year)))

(defn on-uranus
  [seconds]
  (/ seconds (* 84 earth-year)))

(defn on-neptune
  [seconds]
  (/ seconds (* 164.79 earth-year)))