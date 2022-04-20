(ns clojure-noob.pluralsight.functional-test
  (:require [clojure.test :refer :all]
            [clojure-noob.pluralsight.functional :refer :all]))

(deftest fac-test
  (is
    (= 1 (fac 1)))
  (is
    (= 6 (fac 3)))
  (is
    (= 24 (fac 4)))
)

