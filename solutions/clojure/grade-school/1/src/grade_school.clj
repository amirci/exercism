(ns grade-school)

(defn grade 
  [db number]
  (or (db number) [])
)

(defn add 
  [db name gr]
  ;;(def stored (or (db grade) []))
  (def stored (grade db gr))
  (assoc db gr (conj stored name)))



(defn sorted
  [db]
  (into (sorted-map)
    (for [[k v] db] [k (vec (sort v))])))