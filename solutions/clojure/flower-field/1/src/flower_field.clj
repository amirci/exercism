(ns flower-field
  (:require [clojure.string :as st]))

(defn- find-empty-spaces [board]
  (for [row (range (count board))
        col (range (count (first board)))
        :when (= \space (get-in board [row col]))]
    [row col]))

(defn- neighbours [[a b]]
  (for [x [-1 0 1] y [-1 0 1] :when (not= [0 0] [x y])]
    [(+ a x) (+ b y)]))

(defn- update-flower-count [source target empty-space]
  (->> empty-space
       neighbours
       (keep (partial get-in source))
       (filter (partial = \*))
       count
       (#(if (zero? %) \space %))
       (assoc-in target empty-space)))

(defn- update-adjacent-flowers [board empty-spaces]
  (reduce (partial update-flower-count board) board empty-spaces))


(defn draw
  "Fills in the number of adjacent flowers for each empty square in the board."
  [board]
  (let [board (->> board
                   st/split-lines
                   (mapv vec))]
    (->> board
         find-empty-spaces
         (update-adjacent-flowers board)
         (map st/join)
         (st/join "\n"))))
