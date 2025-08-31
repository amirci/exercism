(ns dominoes)

(defn multiset [dms]
  (->> dms
       (map sort)
       frequencies))

(defn ms-disj [ms e]
  (let [total (ms e)]
    (cond
      (nil? total) ms
      (< 1 total) (update ms e dec)
      :else (dissoc ms e))))

(defn match? [n [a b]]
  (or (= n a) (= n b)))

(def flip reverse)

(defn find-matches [n dms]
  (->> dms
       keys
       (filter (partial match? n))
       set))


(defn- start-option
  [ms d]
  (let [pending (ms-disj ms d)
        flp (flip d)]
    (cond-> [[[d] pending]]
      (not= d flp) (conj [[flp] pending]))))

(defn starting-options [dms]
  (let [ms (multiset dms)]
    (->> dms
         (mapcat (partial start-option ms))
         (into ()))))

(defn update-chain [chain pending [_ d :as piece]]
  (let [[_ b] (last chain)
        lst (cond-> piece
              (= b d) flip)]
    [(conj chain lst)
     (ms-disj pending piece)]))

(defn new-matches [chain pending matches]
  (map (partial update-chain chain pending) matches))

(defn winner? [chain]
  (let [[a] (first chain)
        [_ b] (last chain)]
    (= a b)))

(defn can-chain? [dms]
  (if (empty? dms)
    dms
    (loop [[[chain pending] & rst :as stack] (starting-options dms)]
      (cond
        (nil? stack) nil
        (and
         (empty? pending) (winner? chain)) chain
        (empty? pending) (recur rst)
        :else (let [[_ b] (last chain)
                    matches (find-matches b pending)]
                (cond-> rst
                  (seq matches) (into (new-matches chain pending matches))
                  :always recur))))))


