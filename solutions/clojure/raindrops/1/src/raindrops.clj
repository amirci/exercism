(ns raindrops)

(defn multiple? [num div]
  (zero? (mod num div)))


(defn multiples [num]
  (cond-> []
    (multiple? num 3) (conj "Pling")
    (multiple? num 5) (conj "Plang")
    (multiple? num 7) (conj "Plong")))


(defn convert [num]
  (let [ms (multiples num)]
    (cond->>  ms
      (empty? ms) (cons num)
      :always (apply str))))


