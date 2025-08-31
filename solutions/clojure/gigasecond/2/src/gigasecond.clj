(ns gigasecond
  (:import [java.time LocalDateTime]))


(def gigsnds (Math/pow 10 9))

(def ->vec
  (juxt
   #(.getYear %)
   #(.getMonthValue %)
   #(.getDayOfMonth %)))

(defn from-vec [y m d]
  (LocalDateTime/of y m d 0 0 0))

(defn from [year m d]
  (-> (from-vec year m d)
      (.plusSeconds gigsnds)
      (.toLocalDate)
      ->vec))


