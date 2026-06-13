(ns pythagorean-triplet)

(defn- square [a] (* a a))

(defn find-pythagorean-triplets
  "Given an integer N, it returns all Pythagorean triplets
  for which a + b + c = N."
  [N]
  (vec
   (for [a (range 1 (inc (quot N 3)))
         b (range (inc a) (inc (quot (- N a) 2)))
         :let [c (- N a b)]
         :when (= (+ (square a) (square b)) (square c ))]
     [a b c])))
