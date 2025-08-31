(ns rna-transcription)

(defn r-map
  [r]
  (or
   (get {\C \G \G \C \A \U \T \A} r)
   (assert false "Invalid RNA seq")))

(defn to-rna
  [rseq]
  (apply str (map r-map rseq)))
