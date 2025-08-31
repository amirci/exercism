(ns change)


(defn min-cached [coins cached amt]
  (->> coins
       (filter (partial >= amt))
       (map #(-> (- amt %)
                 (cached [])
                 (conj %)))
       (apply min-key count)
       (assoc cached amt)))


(defn ensure-change [amount coins]
  (when (or (neg? amount)
            (and (pos? amount) (every? (partial < amount) coins)))
    (throw (IllegalArgumentException. "cannot change"))))

(defn issue [amount coins]
  (ensure-change amount coins)

  (let [coins (sort coins)]
    (->> (range 1 (inc amount))
         (reduce (partial min-cached coins) {})
         (#(get % amount)))))


