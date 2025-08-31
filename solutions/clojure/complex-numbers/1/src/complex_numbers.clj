(ns complex-numbers)

(def real first)

(def imaginary second)

(defn square [n] (* n n))

(defn abs [nbr]
  (->> nbr
       (map square)
       (apply +)
       Math/sqrt))

(defn conjugate [[a b]]
  [a (* b -1)])

(defn add [n1 n2]
  (map + n1 n2))

(defn sub [n1 n2]
  (map - n1 n2))

(defn mul [[a b] [c d]]
  [(- (* a c) (* b d)) (+ (* b c) (* a d))])

(defn div [[a b] [c d]] 
  (let [den (float (+ (square c) (square d)) )]
    [(/ (+ (* a c) (* b d)) den)
     (/ (- (* b c) (* a d)) den)]))
