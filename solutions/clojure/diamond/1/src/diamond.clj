(ns diamond)

(defn width [c]
  (+ 1 (* 2 (- (int c) (int \A)))))

(defn half-line
  [w dist c]
  (let [q (- w dist)]
    (cond->> (format (str "%-" q "s") c)
      (pos? dist) (format (str "%" dist "s%s") \space))))

(defn line
  [q dist c]
  (cond
    (zero? q)  (str c)
    (= dist q) (format (str "%" q "s%s%" q "s") \space c \space)
    :else      (let [s (half-line q dist c)]
                 (->> s 
                      reverse
                      (apply str)
                      (format "%s %s" s)))))

(defn prev-char
  [c i]
  (char (- (int c) i)))

(defn half-diamond
  [c]
  (let [w (width c)
        q (quot w 2)]
    (->> (range (inc q))
         (map (juxt identity (partial prev-char c)))
         (map #(apply line q %)))))

(defn diamond
  [c]
  (let [h (half-diamond c)]
    (->> h
         (drop 1)
         reverse
         (#(concat % h)))) )

