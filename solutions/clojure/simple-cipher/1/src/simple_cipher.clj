(ns simple-cipher)

(defn rand-key
  "Returns a random key"
  []
  (->> (range 100)
       (map (fn [_] (rand-nth "abcdefghijklmnopqrstuvwxyz")))
       (apply str)))

(defn dist-a [c]
  (- (int c) (int \a)))

(defn- shift-char [[c dist]]
  (let [d2 (dist-a c)]
    (char (+ (int \a) (mod (+ d2 dist) 26)))))

(defn shift-text
  [f k text]
  (->> k
       (map dist-a)
       cycle
       (map vector text)
       (map f)
       (apply str)))

(def encode
  "Encodes text using the specified key"
  (partial shift-text shift-char))


(def unshift-char (comp shift-char #(update % 1 * -1)))

(def decode
  "Decodes text using the specified key"
  (partial shift-text unshift-char))
