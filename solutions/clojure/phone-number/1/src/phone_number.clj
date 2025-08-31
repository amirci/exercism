(ns phone-number)

(use '[clojure.string :as str])

(defn number
  [src]
  (def num (str/join (re-seq #"\d+" src)))
  (def length (count num))
  (def invalid "0000000000")
  (def starts-with-one (str/starts-with? num "1"))
  (cond
    (== length 10) num
    (== length 11) (if starts-with-one (subs num 1) invalid)
    :else invalid))


(defn area-code
  [phone]
  (subs (number phone) 0 3))

(defn pretty-print
  [phone]
  (def num (number phone))
  (def area (subs num 0 3))
  (def p1 (subs num 3 6))
  (def p2 (subs num 6))
  (str "(" area ") " p1 "-" p2))