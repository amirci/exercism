(ns interest-is-interesting)

(defn interest-rate
  "Returns the interest rate given a bank account balance
  Pre: takes a bank account balance
  Post: return the interest rate as follows:
  - -3.213% for a negative balance.
  - 0.5% for a positive balance less than `1000` dollars.
  - 1.621% for a positive balance greater or equal than `1000` dollars and less than `5000` dollars.
  - 2.475% for a positive balance greater or equal than `5000` dollars."
  [balance]
  (condp <= balance
    5000 2.475
    1000 1.621
    0 0.5
    -3.213))

(defn abs [x]
  (cond-> x (neg? x) -))

(defn annual-balance-update
  "Calculates the total balance after adding the interest for the year"
  [balance]
  (let [interest (interest-rate balance)
        mul (bigdec (/ interest 100))]
    (+ balance (* (abs balance) mul))))


(defn amount-to-donate
  "Calculated the amount to donate based on the tax free percentage"
  [balance tax-free-percentage]
  (if (neg? balance)
    0
    (int
     (* 2 balance (/ tax-free-percentage 100)))))
