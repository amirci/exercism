(ns leap)

(defn is-mul-of?
  [n mul]
  (== 0
      (rem n mul)))

(defn leap-year?
  [year]
  (and (is-mul-of? year 4)
       (or
         (not (is-mul-of? year 100))
         (is-mul-of? year 400))))