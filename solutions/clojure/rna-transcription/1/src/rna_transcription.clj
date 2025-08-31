(ns rna-transcription)

(defn to-rna
  [rseq]
  (defn r-map [r]
    (or 
      ({\C \G
        \G \C
        \A \U
        \T \A} r)
      (assert false "Invalid RNA seq")))
  (apply str (map r-map rseq))
)