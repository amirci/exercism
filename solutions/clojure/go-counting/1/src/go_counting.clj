(ns go-counting
  (:require [clojure.set :as set]))

(defn- neighbours [[row col]]
  [[(dec row) col]
   [(inc row) col]
   [row (dec col)]
   [row (inc col)]])

(def ^:private empty-stone? (partial = \space))

(def ^:private stone-at get-in)

(def ^:private empty-at? (comp empty-stone? stone-at))

(defn- inspect-neighbours [grid visited? pos]
  (let [to-visit (->> pos neighbours (remove visited?))]
    {:to-visit to-visit
     :bordering-owners (->> to-visit
                            (keep (partial stone-at grid))
                            (remove empty-stone?)
                            set)}))

(defn- connected-empty
  ([grid pos] (take 2 (connected-empty grid [#{} #{} #{}] pos)))
  ([grid [connected owners visited?] pos]
   (let [visited? (conj visited? pos)]
     (if (empty-at? grid pos)
       (let [{:keys [to-visit bordering-owners]} (inspect-neighbours grid visited? pos)
             owners (into owners bordering-owners)
             connected (conj connected pos)]
         (->> to-visit
              (filter (partial empty-at? grid))
              (reduce
               (partial connected-empty grid)
               [connected owners visited?])))
       [connected owners visited?]))))



(def ->owner {\B :black \W :white})

(defn- reverse-coordinates [stones]
  (->> stones
       (map reverse)
       set))

(defn territory [grid [col row]]
  (assert (and (< -1 col (count (first grid))) (< -1 row (count grid))))
  (let [[stones owners] (connected-empty grid [row col])
        owner-char (when (= 1 (count owners)) (first owners))]
    {:stones (reverse-coordinates stones) :owner (->owner owner-char)}))

(defn- all-coordinates [grid]
  (for [row (range 0 (count grid))
        col (range 0 (count (first grid)))]
    [row col]))

(def ^:private new-names {:black :black-territory :white :white-territory nil :null-territory})


(defn- collect-territories [grid coll]
  (->> coll
       (reduce
        (fn [{:keys [seen] :as acc} [row col :as pos]]
          (if (seen pos)
            acc
            (let [{:keys [stones owner]} (territory grid [col row])
                  territory-k (new-names owner)]
              (-> acc
                  (update :seen set/union (reverse-coordinates stones))
                  (update territory-k set/union stones)))))
        {:seen #{} :black-territory #{} :white-territory #{} :null-territory #{}})
       (#(dissoc % :seen))))

(defn territories [grid]
  (->> grid
       all-coordinates
       (filter (partial empty-at? grid))
       (collect-territories grid)))
