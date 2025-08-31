(ns wordy)


(defn parse-what-is [[w1 w2 & wds]]
  (when (= ["What" "is"] [w1 w2])
    [wds]))


(def ops
  {+ ["plus"]
   - ["minus"]
   * ["multiplied" "by"]
   / ["divided" "by"]})


(defn ->tokens [s]
  (re-seq #"\w+|-?\d+" s))


(defn match [wds [f exp]]
  (let [[candidate rst] (split-at (count exp) wds)]
    (when (= exp candidate)
      [rst f])))


(defn parse-op [wds]
  (->> ops
       (map (partial match wds))
       (filter seq)
       first))


(defn <*> [& ps]
  (fn [wds]
    (reduce
     (fn [[tokens result] p]
       (if-let [[tokens* v] (p tokens)]
         [tokens* (cond-> result v (conj v))]
         (reduced nil)))
     [wds []]
     ps)))

(defn parse-nbr [[nbr & rst]]
  (when (re-matches #"-?\d+" nbr)
    [rst (read-string nbr)]))


(defn parse-many1 [p]
  (fn [wds]
    (loop [result [] tokens wds]
      (if-let [[tokens* v] (and (seq tokens) (p tokens))]
        (recur (cond-> result v (conj v)) tokens*)
        (when (seq result) [tokens result])))))


(def expr-parser
  (<*> parse-what-is
       parse-nbr
       (parse-many1 (<*> parse-op parse-nbr))))


(defn evaluate [s]
  (let [[pending [nbr ops]] (expr-parser (->tokens s))]
    (when (or (seq pending) (nil? nbr)) (throw (IllegalArgumentException.)))
    (reduce
     (fn [acc [f n]] (f acc n))
     nbr
     ops)))

