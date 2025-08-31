(ns phone-number)

(defn invalid-check
  [s]
  (if (seq s)
    s
    ["000" "000" "0000"]))

(defn split-nbr [s]
  (->> (clojure.string/replace s #"\D" "")
       (re-matches #"1?(\d{3})(\d{3})(\d{4})")
       (drop 1)
       invalid-check))

(defn number
  [s]
  (->> s
       split-nbr
       (apply str)))

(def area-code (comp first split-nbr))

(defn pretty-print
  [s]
  (apply format "(%s) %s-%s" (split-nbr s)))

