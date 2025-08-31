(ns beer-song)

(def pluralize #(if (> % 2) "bottles" "bottle"))

(defn verse
  [n]
  (condp = n
    0 (format "%s\n%s\n"
              "No more bottles of beer on the wall, no more bottles of beer."
              "Go to the store and buy some more, 99 bottles of beer on the wall.")
    1 (format "%s\n%s\n"
              "1 bottle of beer on the wall, 1 bottle of beer."
              "Take it down and pass it around, no more bottles of beer on the wall.")
    (format "%d %s, %d bottles of beer.\n%s, %d %s of beer on the wall.\n"
            n
            "bottles of beer on the wall"
            n
            "Take one down and pass it around"
            (dec n)
            (pluralize n))))

(defn sing
  ([n]   (sing n 0))
  ([n k] (->> (range n (dec k) -1)
              (map verse)
              (clojure.string/join "\n"))))

