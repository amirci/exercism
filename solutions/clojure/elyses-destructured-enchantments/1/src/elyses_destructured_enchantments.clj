(ns elyses-destructured-enchantments)

(defn first-card
  "Returns the first card from deck."
  [[fst]]
  fst)

(defn second-card
  "Returns the second card from deck."
  [[_ snd]]
  snd)

(defn swap-top-two-cards
  "Returns the deck with first two items reversed."
  [[fst snd & rst]]
  (concat [snd fst] rst))

(defn discard-top-card
  "Returns a vector containing the first card and
   a vector of the remaining cards in the deck."
  [[fst & rst]]
  [fst rst])

(def face-cards
  ["jack" "queen" "king"])

(defn insert-face-cards
  "Returns the deck with face cards between its head and tail."
  [[fst & rst]]
  (concat (some-> fst vector)
          face-cards
          rst))
