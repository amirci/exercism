(ns binary-search-tree)

(def value :value)

(defn singleton [n] {:value n})

(declare insert)

(defn singleton-or-follow [bst n]
  (if (value bst)
    (insert n bst)
    (singleton n)))

(defn insert [n {:keys [value] :as bst}]
  (cond
    (< n value) (update bst :left singleton-or-follow n)
    (< value n) (update bst :right singleton-or-follow n)
    :else {:value n :left bst}))

(def left :left)

(def right :right)

(defn to-list [{:keys [value left right] :as bst}]
  (if-not bst
    []
    (concat (to-list left) [value] (to-list right))))


(defn from-list [[fst & rst]]
  (reduce
   #(insert %2 %1)
   (singleton fst)
   rst))
