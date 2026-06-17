(ns killer-sudoku-helper)

(defn- find-sum-up-to [result target digits remaining]
  (cond
    (pos? remaining) (->> digits
                          (filter #(<= % target))
                          (mapcat #(find-sum-up-to (conj result %)
                                                   (- target %)
                                                   (filter (partial < %) digits)
                                                   (dec remaining))))
    (zero? target) [result]
    :else []))

(def ^:private all-digits (range 1 10))

(defn combinations
  "Returns the valid combinations for a given cage."
  [{:keys [sum size exclude]}]
  (if (= 1 size)
    [[sum]]
    (->> size
         (find-sum-up-to #{} sum (remove (set exclude) all-digits))
         (mapv (comp vec sort))
         sort)))
