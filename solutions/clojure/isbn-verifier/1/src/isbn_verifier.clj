(ns isbn-verifier)

;(x1 * 10 + x2 * 9 + x3 * 8 + x4 * 7 + x5 * 6 + x6 * 5 + x7 * 4 + x8 * 3 + x9 * 2 + x10 * 1) mod 11 == 0

(def replace-x #(clojure.string/replace % "X" "10"))

(def parse-int #(Integer/parseInt %))

(defn valid?
  [parsed]
  (->> parsed
       (map (comp parse-int replace-x))
       (map * (range 10 0 -1))
       (apply +)
       (#(mod % 11))
       zero?))

(defn isbn?
  [isbn]
  (true?
   (if-let [parsed (re-matches #"(\d)-?(\d{3})-?(\d{5})-?([\dX])" isbn)] 
     (->> parsed
          (drop 1)
          (apply str)
          valid?))))

