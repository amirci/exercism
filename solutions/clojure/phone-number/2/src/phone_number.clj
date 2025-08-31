(ns phone-number)

(use '[clojure.string :as str])

(defn number
  [src]
  (let [nmb (str/join (re-seq #"\d+" src))
        length (count nmb)
        invalid "0000000000"
        starts-with-one (str/starts-with? nmb "1")]
        (cond
          (== length 10) nmb
          (== length 11) (if starts-with-one (subs nmb 1) invalid)
          :else invalid)))


(defn area-code
  [phone]
  (subs (number phone) 0 3))

(defn pretty-print
  [phone]
  (let [num (number phone)
        area (subs num 0 3)
        p1 (subs num 3 6)
        p2 (subs num 6)]
        (str "(" area ") " p1 "-" p2)))