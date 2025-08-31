(ns run-length-encoding)

(defn encode
  [[fst & rst :as part]]
  (cond->> fst
    (seq rst) (str (count part))))

(defn run-length-encode
  "encodes a string with run-length-encoding"
  [plain-text]
  (->> plain-text
       (partition-by identity)
       (map encode)
       (apply str)))

(defn expand
  [[_ n ltr]]
  (cond-> ltr
    (seq n) (->>
             (repeat (Integer/parseInt n))
             (apply str))))

(defn run-length-decode
  "decodes a run-length-encoded string"
  [cipher-text]
  (->> cipher-text
       (re-seq #"(\d+)?(\D)")
       (map expand)
       (apply str)))

