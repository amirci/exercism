(ns bob)

(use 'clojure.string)

;; question -> Sure.
;; yell -> Whoa, chill out!
;; without saying -> Fine. Be that way!
;; else -> Whatever.

(defn response-for
  [msg]
  (def response "Whatever.")
  (def is-question (= \? (last (seq msg))))
  (def all-upper 
    (re-matches #"[A-Z]+"
      (join "" (re-seq #"[a-z|A-Z]+" msg))))
  (def all-spaces (empty? (trim msg)))

  (if is-question (def response "Sure."))
  (if all-upper   (def response "Whoa, chill out!"))
  (if all-spaces  (def response "Fine. Be that way!"))
  response
)