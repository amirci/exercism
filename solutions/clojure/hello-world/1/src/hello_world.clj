(ns hello-world
  (:gen-class))

(defn hello
  ([] "Hello, World!")
  ([name] (str "Hello, " name "!"))
)