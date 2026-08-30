(ns protein-translation)

(def codon->protein
  {"AUG" "Methionine"
   "UUU" "Phenylalanine"
   "UUC" "Phenylalanine"
   "UUA" "Leucine"
   "UUG" "Leucine"
   "UCU" "Serine"
   "UCC" "Serine"
   "UCA" "Serine"
   "UCG" "Serine"
   "UAU" "Tyrosine"
   "UAC" "Tyrosine"
   "UGU" "Cysteine"
   "UGC" "Cysteine"
   "UGG" "Tryptophan"})

(def stop-codons #{"UAA" "UAG" "UGA"})

(defn translate-codon [codon]
  (or (codon->protein codon)
      (when (stop-codons codon) "STOP")
      (throw (IllegalArgumentException. "Invalid codon"))))

(defn- codons [rna]
  (->> rna
       (partition-all 3)
       (map (partial apply str))))

(defn- complete-codon? [codon]
  (= 3 (count codon)))

(defn- valid-codon? [codon]
  (and (complete-codon? codon)
       (codon->protein codon)))

(defn- any-invalid? [codons]
  (some (complement valid-codon?) codons))

(defn- take-until-stop [rna]
  (->> rna
       codons
       (take-while (complement stop-codons))))

(defn translate-rna
  "Translates an RNA string into amino acids."
  [rna]
  (let [translatable-codons (->> rna take-until-stop)]
    (when (any-invalid? translatable-codons)
      (throw (IllegalArgumentException. "Invalid codon")))
    (map translate-codon translatable-codons)))
