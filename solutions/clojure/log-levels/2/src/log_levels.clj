(ns log-levels
  (:require [clojure.string :as st]))

(defn parse-log [s]
  (let [[level msg] (st/split s #":")]
    (-> level
        st/lower-case
        (st/replace #"[\[\]]" "")
        (vector (st/trim msg)))))

(def message
  "Takes a string representing a log line
   and returns its message with white space trimmed."
  (comp second parse-log))


(def log-level
  "Takes a string representing a log line
   and returns its level in lower-case."
  (comp first parse-log))

(defn reformat
  "Takes a string representing a log line and formats it
   with the message first and the log level in parentheses."
  [s]
  (apply format "%s (%s)" (reverse (parse-log s))))
