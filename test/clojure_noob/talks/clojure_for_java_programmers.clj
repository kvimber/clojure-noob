(ns clojure-noob.talks.clojure-for-java-programmers
  (:require [clojure.test :refer :all])
)
;; Originally found these videos[1, 2] in the playlist of Rich Hickey talks[3].
;;
;; References:
;; 1. Clojure for Java Programmers Part 1 - Rich Hickey
;;   - https://www.youtube.com/watch?v=P76Vbsk_3J0
;;   - TODO add earlier tests from this
;;     - really only added tests for sequence things at the very end of talk
;; 2. Clojure for Java Programmers Part 2 - Rich Hickey
;;   - https://www.youtube.com/watch?v=hb3rurFxrZ8
;;   - TODO watch & create tests from
;; 3. ____
;;   - having trouble finding a link directly to the playlist
;;   - TODO find link to YouTube playlist of Rich Hickey talks!


(deftest drop-test
  ;; https://clojuredocs.org/clojure.core/drop
  (testing "drop returns a lazy sequence of all but the first n items in coll."
    (is (=
      (drop 3 [9 10 11 12 13])
      '(12 13)
    ))
  )
)

(deftest take-test
  ;; https://clojuredocs.org/clojure.core/take
  (testing "take returns a lazy sequence of the first n items in coll, ..."
    (is (=
      (take 2 [45 46 47 48])
      '(45 46)
    ))
  )
  (testing "take (cont'd): or all items if there are fewer than n"
    (is (=
      (take 6 [45 46 47 48])
      '(45 46 47 48)
    ))
  )
)

(deftest cycle-test
  ;; https://clojuredocs.org/clojure.core/cycle
  (testing "cycle returns a lazy (infinite!) sequence of repetitions of the items in coll."
    (is (=
      (take 5 (cycle ["a" "b"]))
      '("a" "b" "a" "b" "a")
    ))
  )
)

(deftest interleave-test
  ;; https://clojuredocs.org/clojure.core/interleave
  (testing "interleave returns a lazy seq of the first item in each coll, then the second etc."
    (is (=
      (interleave [:a :b :c :d] [3 4 5 6])
      '(:a 3 :b 4 :c 5 :d 6)
    ))
  )
  ;; this test taken from the doc
  (testing "interleave can help build a map out of separate key & value lists"
    (is (=
      (apply assoc {}
        (interleave [:fruit :color :temp] ["grape" "red" "hot"])
      )
      {:fruit "grape" :color "red" :temp "hot"}
    ))
  )
)

(deftest partition-test
  ;; https://clojuredocs.org/clojure.core/partition
  (def partition-result '(
    ( 0  1  2  3)
    ( 4  5  6  7)
    ( 8  9 10 11)
    (12 13 14 15)
    (16 17 18 19)
  ))
  (testing "partition returns a lazy sequence of lists of n items each, at offsets step apart"
    (is (=
      (partition 4 (range 20))
      partition-result
    ))
  )
  (testing "partition drops the remainder in the sequence if it can't fill a partition"
    (is (=
      (partition 4 (range 22))
      partition-result
    ))
  )
)
