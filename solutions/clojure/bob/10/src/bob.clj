(ns bob
  (:require [clojure.string :as s]))

;; question -> Sure.
;; yell -> Whoa, chill out!
;; without saying -> Fine. Be that way!
;; else -> Whatever.

(def question? #(s/ends-with? % "?"))

(defn forceful? 
  [msg]
  (and
   (re-find #"[A-Z]" msg)
   (empty? (re-find #"[a-z]" msg))))

(def silence? empty?)

(defn response-for
  [msg]
  (let [msg (s/trim msg)
        [qs? fcf?] ((juxt question? forceful?) msg)]
    (cond 
      (silence? msg) "Fine. Be that way!"
      (and qs? fcf?) "Calm down, I know what I'm doing!"
      fcf?           "Whoa, chill out!"
      qs?            "Sure."
      :else          "Whatever.")))

