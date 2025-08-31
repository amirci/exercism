(ns largest-series-product)

(defn largest-product [n s] 
  (assert (<= n (count s)))
  (assert (< -1 n))
  (let [dts (map #(Character/digit % 10) s)]
    (assert (not-any? (partial = -1) dts))
    (->> dts
         (partition n 1)
         (map (juxt identity (partial apply *)))
         (#(if (seq %)
             (->> %
                  (apply max-key second)
                  second)
             1)))))

