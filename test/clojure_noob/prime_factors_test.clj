(ns clojure-noob.prime-factors-test
  (:require [clojure.test :refer :all])
)
;; Originally found the Prime Factors Kata in this article[4]. That points out
;; that its source[5] argues that Clojure makes the "state machine" of the prime
;; factors algorithm more explicit, but I think the conclusion was that they
;; don't see it yet.
;;
;; References:
;; 1. Table of prime factors (Wikipedia)
;;   - https://en.wikipedia.org/wiki/Table_of_prime_factors#1_to_100
;; 2. Table-driven Tests in Clojure (michaelwhatcott.com)
;;   - https://michaelwhatcott.com/table-driven-tests-in-clojure/
;;   - discusses shortening repetitive tests of the form `is` -> `doseq` -> `are`
;;   - referencing table driven tests[3]
;; 3. Prefer table driven tests (Dave Cheney) (TODO read)
;;   - https://dave.cheney.net/2019/05/07/prefer-table-driven-tests
;; 4. The Prime Factors Kata in Clojure (michaelwhatcott.com)
;;   - https://michaelwhatcott.com/prime-factors-kata-bach-beethoven-brahams/
;; 5. Loopy (Clean Code Blog) (TODO read)
;;   - https://blog.cleancoder.com/uncle-bob/2020/09/30/loopy.html
;; 6. The Prime Factors Kata (Uncle Bob) (TODO read)
;;   - http://butunclebob.com/ArticleS.UncleBob.ThePrimeFactorsKata


;; Taken from an article on doing this kata in Clojure[1].
;;
;; Isn't complete, fails on cases like n=6, 9, 10, etc.
;;
;; Keeping this around though as a good example of a time where, since the
;; math is a little hard to keep up with (due to things like recursion), I'd
;; like the system to explain its execution to me so that we could debug the
;; process.
(defn prime-factors-of-broken [n]
  (loop [n n, factors []]
    (if (> n 1)
      (if (zero? (mod n 2))
        (recur (/ n 2) (conj factors 2))
        [n]
      )
      factors
    )
  )
)

(defn prime-factors-of [n]
  (loop [n n d 2 factors []]
    (if (> n 1)
      (if (zero? (mod n d))
        (recur (/ n d) d (conj factors d))
        (recur n (inc d) factors)
      )
    factors
    )
  )
)

(defn pf-th-text [n answer]
  "Creates testing headline to explain incorrect test results"
  (str
    "prime-factors: " n " -> " answer "\n"
    "          (= (prime-factors-of " n ") " answer ")"
  )
)

(defn pf-th [n answer]
  "Creates testing statement & condition template"
  (testing (pf-th-text n answer)
    (is (= (prime-factors-of n) answer))
  )
)


;; The tests are really just samples from the table of prime factors[1].
;; NOTE: structure is from [2].
(deftest prime-factors-test-base
  (are [input expected]
      (pf-th input expected)
    1 []
    2 [2]
    3 [3]
    4 [2 2]
    5 [5]
    6 [2 3]
    7 [7]
    8 [2 2 2]
    9 [3 3]
    10 [2 5]
  )
)

;; See prime-factors-test-base
(deftest prime-factors-<hundred
  (are [input expected]
      (pf-th input expected)
    13 [13]
    15 [3 5]
    20 [2 2 5]
    28 [2 2 7]
    48 [2 2 2 2 3]
    57 [3 19]
    72 [2 2 2 3 3]
    84 [2 2 3 7]
    89 [89]
    98 [2 7 7]
  )
)

;; See prime-factors-test-base
(deftest prime-factors-<thousand
  (are [input expected]
      (pf-th input expected)
    134 [2 67]
    197 [197]
    266 [2 7 19]
    384 [2 2 2 2 2 2 2 3]
    739 [739]
    999 [3 3 3 37]
  )
)

