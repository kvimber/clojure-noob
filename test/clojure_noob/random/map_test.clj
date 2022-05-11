(ns clojure-noob.random.map-test
  (:require [clojure.test :refer :all]))

(deftest mapping-to-values
  (testing "mapping to values:"
    ;; random slack conversation in #clojure-help:
    ;;   ref: https://circleci.slack.com/archives/C2B6H0E65/p1652278376031399
    ;; Was just asking for help, saying it's been a while
    ;; since they've programmed, & were having trouble
    ;; just trying to transform
    ;;   [1 2 3]
    ;; into
    ;;   [{:id 1} {:id 2} {:id 3}]
    ;; so these values are all ways to do that suggested.
    ;;
    ;; Thought they would make good map examples/tests
    (def expect [{:id 1} {:id 2} {:id 3}])
    (defn matches
      [operation]
      (is
        (=
          operation
          expect
        )
      )
    )
    (matches
      (map #(hash-map :id %) [1 2 3])
    )
    (matches
      (map #(identity {:id %}) [1 2 3])
    )
    (matches
      (map #(assoc {} :id %) [1 2 3])
    )
  )
)