(ns spiral-matrix)

(defn nbrs
  [start width]
  (let [end (+ start (- (* 4 width) 3))]
    (range start (dec end))))

(defn square-coords
  [x width]
  (let [end (+ x width)]
    (concat 
     (for [y (range x end)] [x y])
     (for [x (range (inc x) end)] [x (dec end)])
     (for [y (range (- end 2) (dec x) -1)] [(dec end) y])
     (for [y (range (- end 2) x -1)] [y x]))))


(defn populate
  [pos start width matrix]
  (case width
    0 matrix
    1 (assoc-in matrix [pos pos] start)
    (let [numbers (nbrs start width)]
      (->> numbers
           (map vector (square-coords pos width))
           (reduce
            (fn [matrix [pos n]] (assoc-in matrix pos n))
            matrix)
           (recur (inc pos) (inc (last numbers)) (- width 2))))))

(defn spiral [n]
  []
  (let [matrix (vec (repeat n (vec (repeat n 0 ))))]
    (populate 0 1 n matrix)))

