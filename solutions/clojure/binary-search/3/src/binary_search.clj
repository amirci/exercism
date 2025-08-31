(ns binary-search)

(defn middle [numbers]
  (quot (count numbers) 2))

(defn search-for
  ([n numbers] (search-for n numbers 0))
  ([n numbers i]
   (let [m (middle numbers)
         [lft [m-val & rgt]] (split-at m numbers)]

     (assert (seq numbers) (str "Element " n " not found"))

     (case (compare n m-val)
       0  (+ i m)
       -1 (recur n lft i)
       1  (recur n rgt (+ i m 1))))))

