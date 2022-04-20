(ns clojure-noob.random.random-test
  (:require [clojure.test :refer :all]))


(deftest functions-vs-special-forms
  (testing "functions:"
    (def test-result (inc (if true 3 5)))
    (is
      (= 4 test-result)
      "should evaluate their args before evaluating themselves"
    )
    (testing "wrapping a special form in a fn turns arg evaluation eager"
      ;; taken from a great slack conversation:
      ;;   https://circleci.slack.com/archives/C2B6H0E65/p1633686388362900
      ;; would like to make this into a test-case here to verify this
      ;;
      ;; (defn if-fn [cond true-branch false-branch] (if cond true-branch false-branch))
      ;; #'user/if-fn
      ;; user=> (if true (println "tru dat") (println "cringe"))
      ;; tru dat
      ;; nil
      ;; user=> (if-fn true (println "tru dat") (println "cringe"))
      ;; tru dat
      ;; cringe
      ;; nil
    )
  )
  (testing "special forms"
    (is
      (= true
        (or true (throw (Exception.)))
      )
      "`or` should short-circuit when a true value is found,\n  leaving the other args unevaluated\n"
    )
  )
)
