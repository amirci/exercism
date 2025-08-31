(ns beer-song)

(def pluralize #(if (> % 2) "bottles" "bottle"))

(defn verse
  [n]
  (condp = n
    0 (str "No more bottles of beer on the wall, no more bottles of beer.\n"
           "Go to the store and buy some more, 99 bottles of beer on the wall.\n")
    1 (str "1 bottle of beer on the wall, 1 bottle of beer.\n"
           "Take it down and pass it around, no more bottles of beer on the wall.\n")
    (format "%d bottles of beer on the wall, %d bottles of beer.\nTake one down and pass it around, %d %s of beer on the wall.\n" n n (dec n) (pluralize n))))

(defn sing
  ([n]   (sing n 0))
  ([n k] (clojure.string/join "\n" (reverse (map verse (range k (+ n 1)))))))
