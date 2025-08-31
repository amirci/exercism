(ns secret-handshake)

(defn commands [nbr]
  (->> ["wink" "double blink" "close your eyes" "jump" reverse]
       (map-indexed vector)
       (reduce
        (fn [coll [bit f]]
          (let [f (if (string? f) #(conj % f) f)]
            (cond-> coll
              (bit-test nbr bit) f)))
        [])))

