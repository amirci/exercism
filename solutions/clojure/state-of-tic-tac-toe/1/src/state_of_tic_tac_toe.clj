(ns state-of-tic-tac-toe
  (:require [clojure.set :as cset]))

(defn- char->move [c]
  ({\X :x \O :o} c))

(defn- string->board [lines]
  (reduce
   (fn [board [row line]]
     (reduce
      (fn [board [col c]]
        (let [move (char->move c)]
          (-> board
              (update :xs #(cond-> % (= :x move) (conj [row col])))
              (update :os #(cond-> % (= :o move) (conj [row col]))))))
      board
      (map-indexed vector line)))
   {:xs #{} :os #{}}
   (map-indexed vector lines)))

(def r3 (range 3))

(def rows (mapv #(mapv (partial vector %) r3) r3))

(def cols (mapv (fn [x] (mapv #(vector % x) r3)) r3))

(def diagonals (vector (mapv #(vector % %) r3) (mapv #(vector % (- 2 %)) r3)))

(def winning-combinations (->> (concat rows cols diagonals)
                               (map set)
                               set))

(defn- winner? [combo]
  (some #(cset/subset? % combo) winning-combinations))

(defn- update-totals [{:keys [xs os] :as board}]
  (assoc board
         :turns (+ (count xs) (count os))
         :turns-diff (- (count xs) (count os))))

(defn- illegal [msg]
  (throw (IllegalArgumentException. msg)))

(defn- raise-wrong-turn-x-went-twice [] (illegal "Wrong turn order: X went twice"))

(defn- raise-wrong-turn-o-started [] (illegal "Wrong turn order: O started"))

(defn- raise-impossible-game-should-have-ended []
  (illegal "Impossible board: game should have ended after the game was won"))

(defn gamestate
  "Returns the gamestate of a tic-tac-toe board."
  [board]
  (let [{:keys [turns turns-diff xs os]} (-> board
                                             string->board
                                             update-totals)
        win-xs (winner? xs)
        win-os (winner? os)]
    (cond
      (< 1 turns-diff) (raise-wrong-turn-x-went-twice)
      (neg? turns-diff) (raise-wrong-turn-o-started)
      (and win-xs win-os) (raise-impossible-game-should-have-ended)
      (or win-xs win-os) :win
      (= 9 turns) :draw
      :else :ongoing)))
