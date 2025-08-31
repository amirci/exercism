(ns rna-transcription
  (:require [clojure.string :as s]))

(def ->rna {\C \G \G \C \A \U \T \A})

(defn to-rna
  [dna]
  (let [rna (apply str (map ->rna dna))]
    (assert (= (count rna) (count dna)))
    rna))

