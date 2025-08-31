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


(defn find-matches [n dms]
  (->> dms
       keys
       (filter (partial match? n))
       set))


(defn can-reach? [target from dms]
  (or
   (and (empty? dms) (= target from))
   (let [matches (find-matches from dms)]
     (some
      (fn [[a b :as match]]
        (let [b (if (= a from) b a)
              pending (ms-disj dms match)]
          (can-reach? target b pending)))
      matches))))


(defn can-chain? [[[a b] & more-dms :as dms]]
  (if (empty? dms)
    dms
    (can-reach? a b (multiset more-dms))))


