(ns nucleotide-count)

(def zero-counts {\A 0 \T 0 \C 0 \G 0})

(defn nucleotide-counts
  [strand]
  (->> strand
       frequencies
       (merge zero-counts)))


(def valid? zero-counts)


(defn count-of-nucleotide-in-strand
  [nuc strand]
  (assert (valid? nuc) "Invalid nucleotide")
  ((nucleotide-counts strand) nuc))
