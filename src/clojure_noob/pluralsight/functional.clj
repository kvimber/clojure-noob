(ns clojure-noob.pluralsight.functional
  (:gen-class))

(defn fac
  "Returns the factorial of n, which must be a positive integer.
  example:
    4! = 4 * 3 * 2 * 1"
  [n]
  (if (= n 1)
    1
    (* n (fac (- n 1)))))
