(ns allergies)

(def ^:private allergens
  [:eggs         ; (1)
   :peanuts      ; (2)
   :shellfish    ; (4)
   :strawberries ; (8)
   :tomatoes     ; (16)
   :chocolate    ; (32)
   :pollen       ; (64)
   :cats])       ; (128)

(def ^:private indexed-allergens (map-indexed vector allergens))
(def ^:private allergen-bits (zipmap allergens (range)))

(defn allergies [score]
  (->> indexed-allergens
       (filterv (fn [[index _]] (bit-test score index)))
       (mapv second)))

(defn allergic-to? [score allergy]
  (some->> (allergen-bits allergy)
           (bit-test score)
           boolean))
