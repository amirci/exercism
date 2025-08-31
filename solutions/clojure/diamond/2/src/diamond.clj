(ns diamond)

(defn width [c]
  (+ 1 (* 2 (- (int c) (int \A)))))

(defn spaces
  [n]
  (apply str (repeat n \space)))

(defn middle-pad
  [q c]
  (format (str "%-" q "s %" q "s") c c))

(defn line
  [q dist c]
  (cond
    (zero? q)  (str c)
    (= dist q) (format "%sA%s" (spaces q) (spaces q))
    :else      (let [pad (spaces dist)]
                 (format "%s%s%s" pad (middle-pad (- q dist) c) pad))))

(defn prev-char
  [c i]
  (char (- (int c) i)))

(defn diamond
  [c]
  (let [w (width c)
        q (quot w 2)]
    (->> (range (inc q))
         (concat (range q 0 -1))
         (map (juxt identity (partial prev-char c)))
         (map #(apply line q %)))))

