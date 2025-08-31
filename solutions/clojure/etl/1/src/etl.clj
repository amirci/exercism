(ns etl)

(defn transform
  [values]
  (into {}
        (reduce concat
                (for [[k words] values]
                     (map #(identity [(.toLowerCase %1) k]) words)))))