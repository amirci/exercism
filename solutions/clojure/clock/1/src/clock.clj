(ns clock)

(defn clock->string [clock]
  (format "%02d:%02d"
          (quot clock 60)
          (mod clock 60)))

(def dim (* 24 60))

(defn add-time [clock time]
  (mod (+ clock time) dim))

(defn clock [hours minutes]
  (add-time 0 (+ minutes (* hours 60))))

