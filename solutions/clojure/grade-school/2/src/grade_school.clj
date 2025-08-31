(ns grade-school)

(defn grade 
  [db gr]
  (get db gr []))

(defn add 
  [db name gr]
  (update db gr #(conj (or % []) name)))

(defn sorted
  [db]
  (into (sorted-map)
    (for [[k v] db] [k (vec (sort v))])))
