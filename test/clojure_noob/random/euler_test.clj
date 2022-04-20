(ns clojure-noob.random.euler-test
  (:require [clojure.test :refer :all]))

(defn multiples-of [multiple-of-no top-no]
  (range multiple-of-no top-no multiple-of-no))

(deftest multiples-of-test
  (testing "10 example, by 3s"
    (is
      (= '(3 6 9)
         (multiples-of 3 10))))
  (testing "10 example, by 5s"
    (is
      (= '(5)
         (multiples-of 5 10)))))

(defn euler-01-01 [top-no]
  ;; My first crack at this problem
  (def multiples-of-3 (multiples-of 3 top-no))
  (def multiples-of-5 (multiples-of 5 top-no))
  (reduce + (distinct (concat multiples-of-3 multiples-of-5))))

(defn euler-01-02 [top-no]
  ;; In looking up how to combine two lists for the above example, came upon this post:
  ;;   https://stackoverflow.com/questions/24576431/clojure-sum-up-a-bunch-of-numbers
  ;; they pointed out the `(distinct (concat stuff))` solution. They also pointed out:
  ;; > In Clojure - and at large in functional programming - we avoid assignment as
  ;; > it destroys state history and makes writing concurrent programs a whole lot
  ;; > harder. In fact, Clojure doesn't even support assignment. An atom is a
  ;; > reference type that is thread safe.
  ;; >
  ;; > Another common trait of functional programming is that we try to solve
  ;; > problems as a series of data transformations. In your case you case some
  ;; > data, a list of numbers from 0 to 1000 exclusive, and you need to obtain
  ;; > the sum of all numbers that match a predicate. This can certainly be done
  ;; > by applying data transformations and completely removing the need for
  ;; > assignment. One such implementation is this:
  ;; > ```clojure
  ;; > (->> (range 1000)
  ;; > (filter #(or (= (rem % 3) 0) (= (rem % 5) 0)))
  ;; > (reduce +))
  ;; > ```
  (reduce
    +
    (distinct
      (concat
        (multiples-of 3 top-no)
        (multiples-of 5 top-no)))))

(defn euler-test-help [func top-no answer]
  (is (= answer (func top-no))))

(deftest euler-01-test
  ;; https://projecteuler.net/problem=1
  ;;
  ;; Problem Statement:
  ;;   If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 and 9. The sum of these multiples is 23.
  ;;
  ;;   Find the sum of all the multiples of 3 or 5 below 1000.
  (testing "my first (01): example: 10 -> 23"
    (euler-test-help euler-01-01 10 23))
  (testing "my first (01): example: 16 -> 60"
    ;; 16 -> [3 5 6 9 10 12 15] -> 60
    (euler-test-help euler-01-01 16 60))
  (testing "my first (01): Actual Test: 1000 -> 233168"
    ;; Actual test question: 1000 -> 233168
    (euler-test-help euler-01-01 1000 233168))
  (testing "my first (01): example: 10 -> 23"
    (euler-test-help euler-01-02 10 23))
  (testing "my first (01): example: 16 -> 60"
    ;; 16 -> [3 5 6 9 10 12 15] -> 60
    (euler-test-help euler-01-02 16 60))
  (testing "my first (01): Actual Test: 1000 -> 233168"
    ;; Actual test question: 1000 -> 233168
    (euler-test-help euler-01-02 1000 233168))
  )
