(ns robot-name)

(def numbers "0123456789")
(def letters (map char (range 65 91)))

(def name-exist? (atom #{}))

(defn rnd-s
  [source length]
  (take length
        (repeatedly #(rand-nth source))))

(defn- mk-name
  []
  (->> (rnd-s numbers 3)
       (concat (rnd-s letters 2))
       (apply str)))


(def robot-name :name)

(defn reset-name
  [robot]
  (->> mk-name
       repeatedly
       (drop-while @name-exist?)
       first
       ((fn [new-name]
          (swap! name-exist? conj new-name)
          new-name))
       (swap! robot assoc :name)))


(defn robot
  []
  (->> {:name ""}
       atom
       reset-name))

