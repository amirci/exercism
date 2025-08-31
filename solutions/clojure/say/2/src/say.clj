(ns say)

(def until-20
  (clojure.string/split
   "zero one two three four five six seven eight nine
    ten eleven twelve thirteen fourteen fifteen sixteen
    seventeen eighteen nineteen"
   #"\n? +"))

(def decenas
  (clojure.string/split
   "zero ten twenty thirty forty fifty sixty seventy eighty ninenty"
   #" "))

(def thousands
  (clojure.string/split
   "zero thousand million billion" #" "))

(defn quot-rem
  [n den]
  [(quot n den) (rem n den)])

(defn split-thousands
  [n]
  (->> [n 0]
       (iterate #(quot-rem (first %) 1000))
       (take-while #(not= % [0 0]))
       (drop 1)
       (map second)
       (map-indexed vector)))


(defn ensure-range
  [num]
  (when (neg? num) (throw (IllegalArgumentException. "Not valid negative number")))
  (when (< 999999999999 num) (throw (IllegalArgumentException. "Not valid for above 999999999999"))))

(defn ->words
  [[idx num]]
  (let [[h d] (quot-rem num 100)
        even? (zero? (rem d 10))]
    (clojure.string/trim
     (cond-> ""
       (pos? h)   (str (get until-20 h) " hundred")
       (< 0 d 20) (str " " (get until-20 d))
       (< 19 d)   (str " " (get decenas (quot d 10)))
       (and
        (< 19 d)
        (not even?)) (str "-" (get until-20 (rem d 10)))
       (and
        (pos? num)
        (pos? idx))  (str " " (get thousands idx))))))

(defn number
  [num]
  (ensure-range num)
  (cond
    (zero? num) "zero"
    (< num 20) (->words [0 num])
    :else      (->> (split-thousands num)
                    (map ->words)
                    reverse
                    (clojure.string/join " ")
                    clojure.string/trim)))

