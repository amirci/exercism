(ns queen-attack)

(def empty-board
  (->> \_
       (repeat 8)
       (apply str)
       vec
       (repeat 8)
       vec))


(defn board-string [{:keys [w b]}]
  (cond-> empty-board
    w (assoc-in w \W)
    b (assoc-in b \B)
    :always (->>
             (map (partial interpose \ ))
             (map (partial apply str))
             (interpose "\n")
             (apply str))
    :finally (str "\n")))


(defn can-attack [{:keys [w b]}]
  (let [[r p :as rel-pos] (map #(Math/abs %) (map - w b))]
    (or (some zero? rel-pos) 
        (= r p)))) 
