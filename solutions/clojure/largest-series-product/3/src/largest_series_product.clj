(ns largest-series-product)

(defn illegal-arg-if [pred msg]
  (when pred (throw (IllegalArgumentException. msg))))

(def char->digit #(Character/digit % 10))

(defn seq-or-default [val coll]
  (or (seq coll) val))

(defn largest-product [n s]
  (illegal-arg-if (> n (count s)) "span must not exceed string length")
  (illegal-arg-if (neg? n) "span must not be negative")
  (let [dts (map char->digit s)]
    (illegal-arg-if (some (partial = -1) dts) "digits input must only contain digits")
    (->> dts
         (partition n 1)
         (map (partial apply *))
         (seq-or-default [1])
         (apply max))))

