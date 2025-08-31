(ns beer-song)

(defn verse
  [n]
  (def pluralized (if (> n 2) "bottles" "bottle"))
  (cond 
    (== n 0) (str "No more bottles of beer on the wall, no more bottles of beer.\n"
                  "Go to the store and buy some more, 99 bottles of beer on the wall.\n")
    (== n 1) (str "1 bottle of beer on the wall, 1 bottle of beer.\n"
                  "Take it down and pass it around, no more bottles of beer on the wall.\n")
    :else (str n " bottles of beer on the wall, " 
               n " bottles of beer.\n"
               "Take one down and pass it around, "
               (- n 1) " " pluralized " of beer on the wall.\n")))

(defn sing
  ([n]   (sing n 0))
  ([n k] (clojure.string/join "\n" (reverse (map verse (range k (+ n 1)))))))