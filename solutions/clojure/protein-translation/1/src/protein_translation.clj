(ns protein-translation)


(def codons
  {"Methionine" ["AUG"]
   "Phenylalanine" ["UUU" "UUC"]
   "Leucine" ["UUA" "UUG"]
   "Serine" ["UCU" "UCC" "UCA" "UCG"]
   "Tyrosine" ["UAU" "UAC"]
   "Cysteine" ["UGU" "UGC"]
   "Tryptophan" ["UGG"]
   "STOP" ["UAA" "UAG" "UGA"]})


(def ->protein
  (->> codons
       (mapcat (fn [[p cs]] (map vector cs (repeat p))))
       (into {})))


(def translate-codon ->protein)


(defn translate-rna [s]
  (->> s
       (partition 3)
       (map (partial apply str))
       (map ->protein)
       (take-while (partial not= "STOP"))))

