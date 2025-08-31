(ns atbash-cipher)


(def abc "abcdefghijklmnopqrstuvwxyz")

(def cipher (zipmap abc (reverse abc)))


(defn encode [plain]
  (->> plain
       clojure.string/lower-case
       (filter #(Character/isLetterOrDigit %))
       (map #(cipher % %))
       (partition-all 5)
       (map (partial apply str))
       (clojure.string/join " ")))


