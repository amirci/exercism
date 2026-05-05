(ns game-of-life)

(defn- neighbour-cells [[row col]]
  (for [a [-1 0 1] b [-1 0 1]
        :when (not= [0 0] [a b])]
    [(+ row a) (+ col b)]))


(defn- all-positions [matrix]
  (for [row (range 0 (count matrix))
        col (range 0 (count (first matrix)))]
    [row col]))


(defn- alive? [gen cell]
  (= 1 (get-in gen cell)))


(defn- ->as-alive-set [cells]
  (->> cells
       all-positions
       (filter #(alive? cells %))
       set))

(defn- cells-range [cells]
  [(count cells) (count (first cells))])

(defn- in-range? [[rows cols] [row col]]
  (and (<= 0 row (dec rows)) (<= 0 col (dec cols))))


(defn- ->cells-matrix [[rows cols] alive-cell?]
  (mapv
    #(mapv
      (fn [y] (if (alive-cell? [% y]) 1 0))
      (range cols))
    (range rows)))


(defn- alive-next-gen? [n was-alive?]
  (or (= n 3) (and (= n 2) was-alive?)))


(defn tick
  "Returns the next generation of the cells."
  [cells]
  (let [rng (cells-range cells)
        alive-cells (->as-alive-set cells)]
    (->> (for [[cell n] (frequencies (mapcat neighbour-cells alive-cells))
               :when (alive-next-gen? n (alive-cells cell))]
           cell)
         (filter (partial in-range? rng))
         set
         (->cells-matrix rng))))
