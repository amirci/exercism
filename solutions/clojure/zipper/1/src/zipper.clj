(ns zipper)

(defn from-tree [tree]
  {:tree tree :path []})

(defn value [{:keys [tree path]}] 
  (get-in tree (conj path :value)))

(defn- validate-path [{:keys [tree path] :as zipper}]
  (when (get-in tree path) zipper))

(defn left [zipper]
  (validate-path (update zipper :path conj :left)))

(defn right [zipper]
  (update zipper :path conj :right))

(def to-tree :tree)

(defn up [{:keys [path] :as zipper}]
  (when (seq path)
    (update zipper :path pop)))

(defn- set-new-val [k {:keys [path] :as zipper} new-val]
  (update zipper
          :tree
          assoc-in (conj path k) new-val))

(def set-value (partial set-new-val :value))

(def set-left (partial set-new-val :left))

(def set-right (partial set-new-val :right))
