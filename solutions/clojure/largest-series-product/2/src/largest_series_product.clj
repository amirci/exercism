(ns largest-series-product)

(defn illegal-arg-if [pred msg]
  (when pred (throw (IllegalArgumentException. msg))))

(defn largest-product [n s] 
  (illegal-arg-if (> n (count s)) "span must not exceed string length")
  (illegal-arg-if (neg? n) "span must not be negative")
  (let [dts (map #(Character/digit % 10) s)]
    (illegal-arg-if (some (partial = -1) dts) "digits input must only contain digits")
    (->> dts
         (partition n 1)
         (map (juxt identity (partial apply *)))
         (#(if (seq %)
             (->> %
                  (apply max-key second)
                  second)
             1)))))


