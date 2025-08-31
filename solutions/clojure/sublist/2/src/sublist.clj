(ns sublist)

(defn included? [l1 l2]
  (->> l2
       (partition (count l1) 1)
       (some (partial = l1))))

(defn classify [list1 list2]
  (cond
    (= list1 list2) :equal
    (included? list1 list2) :sublist
    (included? list2 list1) :superlist
    :else :unequal))

