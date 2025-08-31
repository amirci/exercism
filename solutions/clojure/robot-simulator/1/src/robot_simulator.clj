(ns robot-simulator)

(defn robot [coord bearing]
  {:coordinates coord :bearing bearing})

(def dirs
  {:west  [-1 0]
   :north [0  1]
   :south [0  -1]
   :east  [1  0]})

(def rev-dirs (clojure.set/map-invert dirs))

(defn turn-right [dir]
  (let [[x y] (dirs dir)]
    (rev-dirs [y (* -1 x)])))

(defn turn-left [dir]
  (let [[x y] (dirs dir)]
    (rev-dirs [(* -1 y) x])))

(defn advance [coord dir]
  (let [[x y] (dirs dir)]
    (-> coord
        (update :x + x)
        (update :y + y))))

(defn step [{:keys [bearing] :as robot} inst]
  (cond-> robot
    (= \L inst) (update :bearing turn-left)
    (= \R inst) (update :bearing turn-right)
    (= \A inst) (update :coordinates advance bearing)))

(defn simulate [inst robot]
  (reduce step robot inst))

