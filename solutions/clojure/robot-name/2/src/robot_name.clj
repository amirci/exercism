(ns robot-name)

(def numbers "0123456789")
(def letters (map char (range 65 91)))

(defn rnd-s 
  [source length]
  (take length 
        (repeatedly #(rand-nth source))))

(defn- mk-name
  []
  (->> (rnd-s numbers 3)
       (concat (rnd-s letters 2))
       (apply str)))


(defn robot 
  []
  (atom {:name (mk-name)}))

(defn robot-name
  [robot]
  (:name @robot))

(defn reset-name 
  [robot]
  (swap! robot assoc :name (mk-name)))
