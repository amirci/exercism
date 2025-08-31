(ns nucleotide-count)


(defn nucleotide-counts
  [strand] 
  (merge {\A 0 \T 0 \C 0 \G 0}
         (frequencies strand)))

(defn count
  [n strand]
  (def is-invalid (not (.contains "GACT" (str n))))
  (or 
    (and is-invalid (throw (Exception. "Invalid nucleotide")))
    ((nucleotide-counts strand) n)
    0))