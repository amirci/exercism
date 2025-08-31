(ns roman-numerals)

(def sm
  (sort-by first >
           (hash-map 1000 \M 900 "CM" 500 \D 400 "CD"
                     100 \C 90 "XC" 50 \L 40 "XL"
                     10 \X 9 "IX" 5 \V 4 "IV" 1 \I)))

(defn find-r [nbr]
  (first (filter (fn [[n]] (<= n nbr)) sm)))

(defn rmn-step [[nbr]]
  (when (pos? nbr)
    (let [[n s] (find-r nbr)
          q (quot nbr n)]
      [(- nbr (* q n)) (repeat q s)])))

(defn numerals [nbr]
  (->> [nbr]
       (iterate rmn-step)
       (take-while seq)
       (mapcat second)
       (apply str)))

