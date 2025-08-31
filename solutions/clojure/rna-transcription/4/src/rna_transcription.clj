(ns rna-transcription
  (:require [clojure.string :as s]))

(def r-map {\C \G \G \C \A \U \T \A})

(defn to-rna
  [rseq]
  (let [trans (map r-map rseq)]
    (assert (empty? (filter nil? trans)))
    (apply str trans)))

