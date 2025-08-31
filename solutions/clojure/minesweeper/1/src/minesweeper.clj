(ns minesweeper)

(def neighbours
  (for [x [-1 0 1] y [-1 0 1] :when (not= [x y] [0 0])] [x y]))

(defn add-pos [p1 p2] (map + p1 p2))

(defn adjacent-squares
  [pos]
  (map add-pos (repeat pos) neighbours))

(defn line->board
  [board [i line]]
  (->> line
       (map-indexed vector)
       (reduce (fn [board [j c]] (assoc board [i j] c)) board)))

(defn ->board [lines]
  (->> lines
       clojure.string/split-lines
       (map-indexed vector)
       (reduce line->board {})))

(def bomb? (partial = \*))

(defn zero->space [n]
  (if (zero? n) \space n))

(defn add-bomb-count
  [board pos]
  (->> pos
       adjacent-squares
       (map board)
       (filter bomb?)
       count
       zero->space
       (assoc board pos)))

(defn ->lines
  [board]
  (if (empty? board)
    ""
    (let [cols (apply max (map second (keys board)))]
      (->> board
           (sort-by first)
           (map second)
           (partition (inc cols))
           (map (partial apply str))
           (clojure.string/join "\n")))))

(defn draw [lines]
  (let [board (->board lines)]
    (->> board
         (filter (complement (comp bomb? second)))
         (map first)
         (reduce add-bomb-count board)
         ->lines)))

