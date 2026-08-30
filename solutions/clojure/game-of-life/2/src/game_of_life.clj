(ns game-of-life)

(defn- cells-range [cells]
  [(count cells) (count (first cells))])

(defn- alive? [gen cell]
  (= 1 (get-in gen cell)))

(defn- full-grid->alive-set [cells]
  (->> (for [row (range 0 (count cells)) col (range 0 (count (first cells)))] [row col])
       (filter (partial alive? cells))
       set))

(defn- in-range? [[max-rows max-cols] [row col]]
  (and (< -1 row max-rows) (< -1 col max-cols)))

(defn- alive-set->full-grid [[max-rows max-cols] alive-cell?]
  (mapv
   #(mapv
     (fn [y] (if (alive-cell? [% y]) 1 0))
     (range max-cols))
   (range max-rows)))

(defn- should-live-next-gen? [n was-alive?]
  (or (= n 3) (and (= n 2) was-alive?)))

(defn- neighbour-cells [[row col]]
  (for [a [-1 0 1] b [-1 0 1] :when (not= [0 0] [a b])]
    [(+ row a) (+ col b)]))

(defn- neighbour-counts [rng alive-cells]
  (->> alive-cells
       (mapcat neighbour-cells)
       (filter (partial in-range? rng))
       frequencies))

(defn tick
  "Returns the next generation of the cells."
  [cells]
  (let [rng (cells-range cells)
        alive-cells (full-grid->alive-set cells)]
    (->> (for [[cell n] (neighbour-counts rng alive-cells) :when (should-live-next-gen? n (alive-cells cell))] cell)
         set
         (alive-set->full-grid rng))))
