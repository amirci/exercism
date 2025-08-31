(ns allergies)

(def a-list
  [:eggs      ; (1)
   :peanuts   ; (2)
   :shellfish ; (4)
   :strawberries ; (8)
   :tomatoes     ; (16)
   :chocolate    ; (32)
   :pollen       ; (64)
   :cats])       ; (128)

(def a-list* (map-indexed vector a-list))

(defn allergies [n]
  (->> a-list*
       (filter (comp (partial bit-test n) first))
       (map second)))

(defn allergic-to? [n alrg]
  (-> n
      allergies
      set
      alrg))




