(ns knapsack)

(defn- update-capacity [weight value best capacity]
  (update best
          capacity
          max
          (+ (best (- capacity weight)) value)))

(defn- add-item [max-weight best {:keys [weight value]}]
  (reduce
   (partial update-capacity weight value)
   best
   (range max-weight (dec weight) -1)))


(defn- empty-capacities [max-weight]
  (vec (repeat (inc max-weight) 0)))

(defn maximum-value
  "Calculates the maximum value that can be packed."
  [max-weight items]
  (->
   (reduce
    (partial add-item max-weight)
    (empty-capacities max-weight)
    items)
   (get max-weight)))

