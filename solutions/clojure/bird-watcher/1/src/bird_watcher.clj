(ns bird-watcher)

(def last-week [0 2 5 3 7 8 4])

(def birds-per-day [2 5 0 7 4 1])

(def today last)

(defn inc-bird [birds]
  (update birds (dec (count birds)) inc))


(def day-without-birds? (comp some? (partial some zero?)))

(defn n-days-count [birds n]
  (->> birds
       (take n)
       (apply +)))

(defn busy-days [birds]
  (->> birds
       (filter (partial < 4))
       count))

(def odd-week? (partial = [1 0 1 0 1 0 1]))
