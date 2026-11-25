(ns squeaky-clean
  (:require [clojure.string :as str]))

(def letter? #(Character/isLetter ^char %))

(def lower-greek? #(<= (int \α) (int %) (int \ω)))

(def space? (partial = \space))

(def underscore? (partial = \_))

(def ctrl? #(Character/isISOControl ^char %))

(def should-uppercase? (partial = \-))

(defn normalize-character [letter]
  (cond
    (space? letter) "_"
    (ctrl? letter) "CTRL"
    (lower-greek? letter) nil
    (underscore? letter) letter
    (letter? letter) letter
    :else nil))


(defn clean-step [{:keys [output uppercase-next?]} letter]
  (if (and (should-uppercase? letter) (not uppercase-next?))
    {:output output :uppercase-next? true}
    (let [text (cond-> (normalize-character letter)
                 uppercase-next? str/upper-case)]
      {:output (str output text)
       :uppercase-next? false})))

(defn clean [s]
  (->> s
       (reduce clean-step {:output "" :uppercase-next? false})
       :output))
