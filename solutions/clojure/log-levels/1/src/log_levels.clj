(ns log-levels
  (:require [clojure.string :as st]))

(defn parse-log [s]
  (let [[level msg] (st/split s #":")]
    (-> level
        st/lower-case
        (st/replace #"[\[\]]" "")
        (vector (st/trim msg)))))

(defn message
  "Takes a string representing a log line
   and returns its message with white space trimmed."
  [s]
  (-> s
      parse-log
      second))


(defn log-level
  "Takes a string representing a log line
   and returns its level in lower-case."
  [s]
  (->> s
       parse-log
       first))


(defn reformat
  "Takes a string representing a log line and formats it
   with the message first and the log level in parentheses."
  [s]
  (apply format "%s (%s)" (reverse (parse-log s))))
