(ns rotational-cipher)

(defn cipher [ch base rotation]
  (-> (int ch)
      (- base)
      (+ rotation)
      (mod 26)
      (+ base)
      char))

(defn rotate-letter [rotation ch]
  (cond-> ch
    (Character/isLowerCase ch) (cipher (int \a) rotation)
    (Character/isUpperCase ch) (cipher (int \A) rotation)))


(defn rotate [source rotation]
  (->> source
       (map (partial rotate-letter rotation))
       (apply str)))

