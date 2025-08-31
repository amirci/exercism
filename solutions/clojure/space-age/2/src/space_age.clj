(ns space-age)

(def a-day (* 24 (* 60 60)))

(def earth-year (* 365.25 a-day))

(defn age-for
  ([f1 seconds] (age-for f1 1 seconds))
  ([f1 f2 seconds]
   (/ seconds (* f1 f2))))

(def on-earth (partial age-for earth-year))

(def on-mercury (partial age-for 87.97 a-day))

(def on-venus (partial age-for 224.7 a-day))

(def on-mars (partial age-for 686.98 a-day))

(def on-jupiter (partial age-for 11.86 earth-year))

(def on-saturn (partial age-for 29.4 earth-year))

(def on-uranus (partial age-for 84 earth-year))

(def on-neptune (partial age-for 164.79 earth-year))


