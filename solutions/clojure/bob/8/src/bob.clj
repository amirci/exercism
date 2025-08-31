(ns bob
  (:require
   [clojure.string :as cstr]))

;; question -> Sure.
;; yell -> Whoa, chill out!
;; without saying -> Fine. Be that way!
;; else -> Whatever.

(def is-question? #(cstr/ends-with? % "?"))

(def is-forceful? 
  #(re-matches #"[A-Z]+"
              (cstr/join "" (re-seq #"[a-z|A-Z]+" %))))

(def all-spaces? #(empty? %))

(defn response-for
  [msg]
  (let [msg (cstr/trim msg)]
    (cond 
      (and
        (is-question? msg)
        (is-forceful? msg)) "Calm down, I know what I'm doing!"
      (is-forceful? msg)    "Whoa, chill out!"
      (is-question? msg)    "Sure."
      (all-spaces?  msg)    "Fine. Be that way!"
      :else "Whatever.")))
