(ns dnd-character)


(defn score-modifier
  "Calculates the modifier of the given score."
;; You find your character's constitution modifier by subtracting 10 from your character's constitution, divide by 2 and round down.
  [score]
  (Math/floorDiv (- score 10) 2))

(defn- throw-die
  "Generates a random ability by throwing a 6 side dice"
  []
  (inc (rand-int 6)))

(defn rand-ability []
  (->> (repeatedly throw-die)
       (take 4)
       sort
       rest
       (apply +)))

(def abilities [:strength :dexterity :constitution :intelligence :wisdom :charisma])

(defn- add-hit-points [m]
  (->> m
       :constitution
       score-modifier
       (+ 10)
       (assoc m :hitpoints)))

(defn rand-character
  "Generates a random character."
  []
  (->> (repeatedly rand-ability)
       (take 6)
       (zipmap abilities)
       (add-hit-points)))
