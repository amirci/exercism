(ns bob)

(use 'clojure.string)

;; question -> Sure.
;; yell -> Whoa, chill out!
;; without saying -> Fine. Be that way!
;; else -> Whatever.

(defn response-for
  [msg]
  (def is-question (= \? (last (seq msg))))
  (def all-upper 
    (re-matches #"[A-Z]+"
      (join "" (re-seq #"[a-z|A-Z]+" msg))))
  (def all-spaces (empty? (trim msg)))

  (cond 
    all-upper   "Whoa, chill out!"
    is-question "Sure."
    all-spaces  "Fine. Be that way!"
    :else "Whatever.")
)