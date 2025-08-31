(ns rotational-cipher)


(defn rotate-letter [az uaz ch]
  (or (az ch) (uaz ch) ch))


(def lower-az "abcdefghijklmnopqrstuvwxyz")


(def upper-az (clojure.string/upper-case lower-az))


(defn cipher [az n]
  (let [n (mod n 26)]
    (->> az
         cycle
         (drop n)
         (take 26)
         (map vector az)
         (into {}))))


(defn rotate [source rotation]
  (let [az (cipher lower-az rotation)
        uaz (cipher upper-az rotation)]
    (->> source
         (map (partial rotate-letter az uaz))
         (apply str))))


