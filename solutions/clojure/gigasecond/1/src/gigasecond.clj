(ns gigasecond
  (:import [java.time LocalDateTime]))


(def gigsnds (Math/pow 10 9))


(defn from [year m d]
  (let [date (-> (LocalDateTime/of year m d 0 0 0)
                 (.plusSeconds gigsnds)
                 (.toLocalDate))]
    [(.getYear date)
     (.getMonthValue date)
     (.getDayOfMonth date)]))
