(ns phone-number)

(def invalid-number (apply str (repeat 10 \0)))

(defn- validate
  [[fst & rst :as nbrs]]
  (let [l (count nbrs)]
    (cond
      (= l 10)          (apply str nbrs)
      (and (= l 11)
           (= "1" fst)) (apply str rst))))

(defn- partition-number
  [s]
  (for [[i c] [[0 3] [3 6] [6 10]]]
    (subs s i c)))

(defn number
  [s]
  (-> (re-seq #"\d" s)
      validate
      (or invalid-number)))

(defn area-code
  [s]
  (-> s number (subs 0 3)))

(defn pretty-print
  [s]
  (-> s
      number
      partition-number
      (as-> [area n1 n2]
        (format  "(%s) %s-%s" area n1 n2))))
