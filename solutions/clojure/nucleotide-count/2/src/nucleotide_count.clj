(ns nucleotide-count)

(defn nucleotide-counts
  [strand] 
  (merge {\A 0 \T 0 \C 0 \G 0}
         (frequencies strand)))

(def valid? #(.contains "GACT" (str %)))

(defn count-of-nucleotide-in-strand
  [nuc strand]
  (assert (valid? nuc) "Invalid nucleotide")
  (get (nucleotide-counts strand) nuc 0))
