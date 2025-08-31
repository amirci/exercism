(ns bob
  (:require
   [clojure.string :as cstr]))

;; question -> Sure.
;; yell -> Whoa, chill out!
;; without saying -> Fine. Be that way!
;; else -> Whatever.

(def question? #(cstr/ends-with? % "?"))

(defn forceful? 
  [msg]
  (and
   (seq (re-seq #"[A-Z]" msg))
   (empty? (re-find #"[a-z]" msg))))


(defn response-for
  [msg]
  (let [msg (cstr/trim msg)
        [qs? fcf?] ((juxt question? forceful?) msg)]
    (cond 
      (empty? msg)   "Fine. Be that way!"
      (and qs? fcf?) "Calm down, I know what I'm doing!"
      fcf?           "Whoa, chill out!"
      qs?            "Sure."
      :else          "Whatever.")))
