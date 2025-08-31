(ns squeaky-clean
  (:require [clojure.string :as st]))

(defn clean
  "Replaces spaces with underscores
  Replaces control characters its code is in the range '\u0000' through '\u001F' or in the range '\u007F' through '\u009F' with 'CTRL'
  Omits characters that are not letters
  Replaces kebab with camel case 'à-ḃç' -> 'àḂç'
  Omits lowercase greek letters 
  "
  [s]
  (-> s
      (st/replace #"[\u0000-\u001F\u007F-\u009F]" "CTRL")
      (st/replace #"\-(.)" (comp st/upper-case second))
      (st/replace #" " "_")
      (st/replace #"[^_|^\p{L}]" "")
      (st/replace #"[α-ω]" "")))

