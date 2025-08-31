(ns difference-of-squares)


(defn square [x] (* x x))

(defn sum-of-squares [n]
  (quot (* n (inc n) (inc (* 2 n))) 6))

(defn sum-n [n] (quot (* n (inc n)) 2))

(defn square-of-sum [n]
  (square (sum-n n)))

(defn difference [n]
  (- (square-of-sum n) (sum-of-squares n)))


