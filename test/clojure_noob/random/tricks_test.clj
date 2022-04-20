(ns clojure-noob.random.tricks-test
  (:require [clojure.test :refer :all]))

;; this file holds the testing tricks that I've found but haven't found a place
;;   to put them. A little reference/cheatsheet for myself later


;; My Context:
;; - first saw this in "Functional Programming with Clojure Course" (org-roam)
(deftest test-exception-thrown
  ;; `thrown` is a special form inside an `is` expression
  ;; you can also verify the error message w/ `thrown-with-msg?` special form
  ;; doc: https://clojuredocs.org/clojure.test/is
  (is (thrown?
        ClassCastException
        (+1 "hello")
  ))
)
