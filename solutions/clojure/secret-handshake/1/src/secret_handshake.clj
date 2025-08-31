(ns secret-handshake)

(defn commands [nbr]
  (reduce
   (fn [coll [bit f]]
     (let [f (if (string? f) #(conj % f) f)]
       (cond-> coll
         (pos? (bit-and nbr bit)) f)))
   []
   {1 "wink"
    2 "double blink"
    4 "close your eyes"
    8 "jump"
    16 reverse}))

