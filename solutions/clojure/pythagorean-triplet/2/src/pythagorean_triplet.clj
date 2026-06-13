(ns pythagorean-triplet)

(defn- square [a] (* a a))

(defn find-pythagorean-triplets
  "Given an integer N, it returns all Pythagorean triplets
  for which a + b + c = N."
  [N]
  (vec
   (for [a (range 1 (inc (quot N 3)))
         :let [numerator (* N (- N (* 2 a)))
               denominator (* 2 (- N a))]
         :when (zero? (mod numerator denominator))
         :let [b (quot numerator denominator)
               c (- N a b)]
         :when (and (< a b c)
                    (= (+ (square a) (square b)) (square c)))]
     [a b c])))
