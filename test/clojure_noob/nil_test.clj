(ns clojure-noob.nil-test
  (:require [clojure.test :refer :all]))

(deftest drop-vs-nthrest-nil-punning
  ;; honestly, I still don't really understand what's meant by "nil punning."
  ;;
  ;; it seems to be the idea that a ton of the functions in the standard libs
  ;; around collections can both take & return nil for their work w/o a problem,
  ;; so the idea is that if they get nothing in, it makes sense that they would
  ;; just pass. The podcast below says that arithmetic is nil-intolerant, tho
  ;;
  ;; Refs:
  ;; - https://clojuredesign.club/episode/047-what-is-nil-punning/
  (testing "normal case: tend to do the same thing"
    (def drop-list (drop 3 (range 10)))
    (def nthrest-list (nthrest (range 10) 3))
    (is (= drop-list nthrest-list))
  )
  (testing "differ: nil-punning"
    (is (= ()  (drop    3 nil)))
    (is (= nil (nthrest nil 3)))
  )
)
