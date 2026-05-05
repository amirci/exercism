(ns saddle-points)

(defn- transpose [trees]
  (apply mapv vector trees))


(defn- all-positions [trees]
  (for [row (range 0 (count trees))
        col (range 0 (count (first trees)))]
    [row col]))


(defn- heights [trees]
  {:trees trees
   :rows-max (mapv #(apply max %) trees)
   :cols-min (mapv #(apply min %) (transpose trees))})


(defn- saddle-point? [{:keys [rows-max cols-min trees]} [row col]]
  (= (rows-max row) (cols-min col) (get-in trees [row col])))


(defn- ->one-based-index [[row col]] [(inc row) (inc col)])


(defn saddle-points
  "Returns the saddle points of a matrix.
taller than every tree to the east and west, so that you have the best possible view of the sunrises and sunsets.
shorter than every tree to the north and south, to minimize the amount of tree climbing.
  "
  [trees]
  (if (empty? trees)
      #{}
      (->> trees
           all-positions
           (filter (partial saddle-point? (heights trees)))
           (map ->one-based-index)
           set)))
