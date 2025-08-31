(ns run-length-encoding)

(defn count-gt1
  [coll]
  (let [total (count coll)]
    (when (< 1 total)
      total)))

(defn run-length-encode
  "encodes a string with run-length-encoding"
  [plain-text]
  (->> plain-text
       (partition-by identity)
       (mapcat (juxt count-gt1 first))
       (apply str)))

(defn expand
  [[_ n letter]]
  (let [n (Integer/parseInt (or n "1"))]
    (repeat n letter)))

(defn run-length-decode
  "decodes a run-length-encoded string"
  [cipher-text]
  (->> cipher-text
       (re-seq #"(\d+)?(\D)")
       (mapcat expand)
       (apply str)))
