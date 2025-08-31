(ns robot-name)

(def robots (atom {}))

(def numbers "0123456789")
(def abc (map char (range 65 91)))

(defn rnd-s 
  [source length]
  (reduce str
    (take length 
      (repeatedly #(rand-nth source)))))

(defn mk-name
  []
  (let [pre (rnd-s abc 2)
        post (rnd-s numbers 4)]
    (str pre post)))

(defn robot 
  []
  (let 
    [id (rnd-s numbers 10)]
      (swap! robots assoc id (mk-name)) 
      id))

(defn robot-name
  [id]
  (@robots id))

(defn reset-name 
  [id]
  (let [name (mk-name)]
    (swap! robots assoc id name)))