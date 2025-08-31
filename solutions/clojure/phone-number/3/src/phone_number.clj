(ns phone-number)

(def invalid-number (apply str (repeat 10 \0)))

(defn- remove-non-digits
  [s]
  (->> s
       (filter #(Character/isDigit %))
       (apply str)))

(defn- validate
  [[fst & rst :as s]]
  (let [l (count s)]
    (cond
      (= l 10) s
      (and (= l 11) (= fst \1)) (apply str rst)
      :else nil)))

(defn- partition-number
  [s]
  (for [[i c] [[0 3] [3 6] [6 10]]]
    (subs s i c)))

(defn number
  [s]
  (-> s
      remove-non-digits
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
        (str "(" area ") "  n1 "-" n2))))