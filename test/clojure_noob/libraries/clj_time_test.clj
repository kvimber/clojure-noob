(ns clojure-noob.libraries.clj-time-test
  (:require
    [clojure.test :refer :all]
    [clj-time.core :as time]
  )
)

;; HELPER FUNCTIONS :: HELPER FUNCTIONS

(defn trial-interval-once [i]
  (let [t (time/now)]
    (try
      (time/interval nil t)
      nil
      (catch IllegalArgumentException _
        [i t]
      )
    )
  )
)

(defn trial-interval-race []
  (count (keep
    trial-interval-once
    (range 1000000)
  ))
)

;; TESTS :: TESTS :: TESTS

(deftest interval-bad-times
  ;; reductive case: an obvious example that shows why the more subtle
  ;; failure in the next test case happens
  (testing "on purpose: set end time before start time"
    (def seconds-three (time/seconds 3))
    (def ago-three-seconds (time/minus (time/now) seconds-three))
    (is (thrown-with-msg?
      java.lang.IllegalArgumentException
      #"end instant must be greater than the start instant"
      (time/interval nil ago-three-seconds)
    ))
  )
  ;; 2023.06.20: got nerd-sniped by a conversation in CircleCI's #clojure-help
  ;;   slack channel[1] about flaky tests. They were caused by trying to use two
  ;;   different ways to tell the `interval` function to use now (`nil` & a
  ;;   previously saved `now` invocation). Due to this, even if run right next
  ;;   to each other, in some cases the end time will take place before the
  ;;   begin time of the interval, which will throw 
  ;;
  ;; 1. https://circleci.slack.com/archives/C2B6H0E65/p1687198324893249
  (testing "accidental race condition: saving an end-NOW before using `nil` as start-NOW"
    (def error-count (trial-interval-race))
    (is (> error-count 0))
    )
  )
