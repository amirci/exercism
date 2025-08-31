(ns phone-number)

(def invalid-number (repeat 10 \0))

(def pn-10? (partial = 10))

(def pn-11? (comp (partial = [11 "1"]) vector))

(defn validate-digits
  [[fst & rst :as digits]]
  (let [l (count digits)]
    (cond
      (pn-10? l) digits
      (pn-11? l fst) rst
      :else invalid-number)))

(defn number
  [s]
  (->> s
       (re-seq #"\d")
       validate-digits
       (apply str)))

(def split-nbr
  (comp
   (juxt #(subs % 0 3)
         #(subs % 3 6)
         #(subs % 6))
   number))

(def area-code (comp first split-nbr))

(defn pretty-print
  [s]
  (apply format "(%s) %s-%s" (split-nbr s)))

