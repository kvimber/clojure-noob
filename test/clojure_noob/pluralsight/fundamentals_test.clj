(ns clojure-noob.pluralsight.fundamentals-test
  (:require [clojure.test :refer :all]
            [clojure-noob.pluralsight.fundamentals1 :refer :all]))

;; (deftest a-test
;;   (testing "FIXME, I fail."
;;     (is (= 0 1))))

;; (deftest name & body)
;;   defines a test fn w/no args
;;   doc: https://clojuredocs.org/clojure.test/deftest
;; (testing string & body)
;;   adds a new string to the list of testing contexts. May be nested, but must
;;     occur inside a test function (deftest).
;;   doc: https://clojuredocs.org/clojure.test/testing
;; (is form) (is form msg)
;;   Generic assertion macro. 'form' is any predicate test.
;;     'msg' is an optional message to attach to the assertion
;;   doc: https://clojuredocs.org/clojure.test/is

(deftest messenger-closes-over-hello
  (testing "messenger-builder fn closes over the string 'hello'"
    (def hello-er (messenger-builder "Hello"))
    (is (= "Hello world!" (hello-er "world!")))
  )
)

;; maps testing
(deftest immutability-map-doesnt-update
  (testing "immutability: map doesn't update under operations"
    (def zipcode-original 27705)
    (def jdoe {:name "John Doe" :address {:zip zipcode-original}})
    (is (=
      zipcode-original
      (get-in jdoe [:address :zip])
    ))
    (assoc-in jdoe [:address :zip] 27514)
    (update-in jdoe [:address :zip] inc)
    (is (=
      zipcode-original
      (get-in jdoe [:address :zip])
    ))
  )
)

(deftest sequential-destructuring-is-a-thing
  (testing "sequential destructuring"
    (def stuff [3 4 5 6])
    (testing "works as expected with typical values"
      (let [[a b c] stuff]
        (is (= 3 a))
        (is (= 4 b))
        (is (= 5 c))))
    (testing "'_' ignores values by convention"
      (let [[a b _ d] stuff]
        (is (= 3 a))
        (is (= 4 b))
        ;; (is (= c nil)) ; trying to ref =c= gives an error
        (is (= 6 d))))
    (testing "binding more values than in collection binds them to nil"
      (let [[a b c d e] stuff]
        (is (= nil e))))
    (testing "'&' starts a collection w/rest of args"
      (let [[& stuff-same] stuff]
        (is (= stuff-same stuff))))
    (testing "can pull args off beginning of collection"
      (let [[a b & stuff-almost] stuff]
        (is (= [5 6] stuff-almost))))))

(deftest associative-destructuring-is-a-thing
  (testing "associative destructuring"
    (def m {:a 7 :b 4}) ;=> #'user/m
    (testing "works with typical values"
      (let [{a :a, b :b} m]
        (is (= 7 a))
        (is (= 4 b))))
    (testing "binds to nil if no value in map"
      (let [{a :a, b :b, c :c} m]
        (is (= nil c))
    ))
    (testing ", :keys"
      (testing "can infer symbols to bind from vector arg"
        (let [{:keys [a b]} m]
          (is (= 7 a))
          (is (= 4 b))
      ))
      (testing "will also bind to nil if no value in map"
        (let [{:keys [c]} m]
          (is (= nil c))
      ))
      (testing "can have a default value set using ':or'"
        (let [{:keys [c] :or {c 3}} m]
          (is (= 3 c))
      ))
      (testing "can setup named args for a fn"
        (defn game [planet & {:keys [human-players computer-players]}]
          (+ human-players computer-players)
        )
        (def total-players
          (game "Mars" :human-players 1 :computer-players 2)
        )
        (is (= 3 total-players))
      )
    )))

(deftest sequences-are-an-abstraction-for-representing-iteration
  ;; backed by a data structure or a fn
  ;; - can be lazy and/or "infinite"
  ;; foundation for a large lib of fns
  (def collection [1 2 3 4])
  (def collection-sequence (seq collection))
  (def collection-empty [])
  (testing "if collection is empty, returns nil sequence"
    (is (= nil (seq collection-empty)))
  )
  (testing "first fn returns the first element"
    (is (= 1 (first collection-sequence)))
    ;; typically, just pass the collection to the seq-fn, in this case first
    ;; these fns will implicitly create a seq from the collection first before applying themselves
    ;; so you almost never see people create sequences for this purpose manually
    (is (= 1 (first collection)))
  )
  (testing "rest fn returns a sequence of the rest of the elements"
    (is (= '(2 3 4) (rest collection)))
  )
  (testing "cons fn pre-pends an element to a collection"
    (is (= '(0 1 2 3 4) (cons 0 collection)))
  )
  ;; uses generator functions in order to lazy-evaluate a sequence
  ;; seq can be infinite (process more than fits in memory)
  ;; SEQUENCE REPL WARNINGS!! be careful!
  ;; - sequences are printed as lists in the REPL, but NOT A LIST
  ;; - infinite sequences take a long time to print:
  ;;   - use =(set! *print-length* 10)= ; only print 10 things (for any given print statement ??)

  ;; clojure's sequence library: fns in clojure.core namespace that deal in sequences
  ;; - is very large
  ;; - generally can be split into two groups:
  ;;   - generators: things that give you lazy sequences
  ;;   - operations: things that do things with sequences
  (testing "generators:"
    (testing "range:"
      (def test-range (range 1 5))
      (testing "includes the first number of the range"
        (is (= 1 (first test-range)))
      )
      (testing "goes up to but doesn't include the last number of the range"
        (is (= 4 (last test-range)))
      )
      (testing "works as expected for a given range"
        (is (= collection test-range))
      )
      (testing "can set a step value to skip values in between"
        (is (= [1 6 11 16] (range 1 20 5)))
      )
    )
    (testing "iterate generates a sequence from a function & start value"
      (def test-iteration (iterate #(* 2 %) 2)) ; careful, is infinite
      (is (= 2 (first  test-iteration)))
      (is (= 4 (second test-iteration)))
    )
    (testing "re-seq generates a sequence from a string of all values that match the given regex"
      (def test-reseq (re-seq #"[aeiou]" "clojure"))
      (is (= ["o" "u" "e"] test-reseq))
    )
  )
  (testing "seq -> seq functions:"
    (def test-take (take 3 (range)))
    (is
      (= [0 1 2] test-take)
      "'take' takes the first X number of values from a sequence"
    )
    (def test-drop (drop 3 (range 5)))
    (is
      (= [3 4] test-drop)
      "'drop' discards the first X values from a sequence"
    )
    (def test-map (map #(* % %) [0 1 2 3 4]))
    (is
      (= [0 1 4 9 16] test-map)
      "'map' applies the given function to every value in the sequence"
    )
    (def test-filter (filter even? (range 1 7)))
    (is
      (= [2 4 6] test-filter)
      "'filter' selects all values that pass a given predicate fn"
    )
  )
  (testing "seq -> value (or data structure) functions:"
    (def test-reduce (reduce + (range 4)))
    (is
      (= 6 test-reduce)
      "'reduce' applies the function over the sequence to create a value"
    )
    (testing "\n  'into' pours sequences into another data structure:"
      (def test-into-set (into #{} "hello"))
      (is
        (= #{\e \h \l \o} test-into-set)
        "the string 'hello' should become the set #{\\e \\h \\l \\o}"
      )
      (def test-into-map (into {} [[:x 1] [:y 2]]))
      (is
        (= {:x 1 :y 2} test-into-map)
        "the vector [[:x 1] [:y 2]] should become the map {:x 1 :y 2}"
      )
    )
    (testing "\n  'some' returns the function evaluation for the first value that is truthy in the sequence"
      ;; some works here b/c maps can be evaluated as functions
      (def test-some (some {2 :b 3 :c} [1 nil 2 3]))
      (is
        (= :b test-some)
        "some should be :b as 2 is the first value in the seq that is a map key"
      )
    )
  )
  ;; Adopting the Sequence Mindset
  ;;
  ;; seq library surface space is big
  ;; most things you want to do are in there somewhere
  ;; if you find yourself explicitly iterating, look for a fn
  ;;
  ;; Use The Clojure Cheatsheet!
  ;;   https://clojure.org/api/cheatsheet
)

(deftest flowcontrol-is-a-thing
  ;; in Java, you have expressions & statements
  ;; - 'if' is a statement: it doesn't return a value
  ;; - expressions (like the ternary operator) DO return a value
  ;; in Clojure, everything is an expression
  ;; - blocks of multiple expressions return their last value
  ;;   - ie. let, do, fn
  ;; - expressions exclusively for side-effects return nil
  ;;   - ie. calling out to network or touching disk

  ;; flow control operators are exprs too
  ;; composable & can be used anywhere
  ;; - thanks to macros (won't get into those here)
  ;; extensible via macros too
  ;; - ie. when-let

  (testing "truthiness"
    (defn truthy? [value]
      (if value :truthy :falsey)
    )
    (defn pr
      " PROBLEM: since (seq []) evals to nil as well, we can't differentiate between it & nil to print.
      You can see it printing nil as the empty string in the doc, but it doesn't seem to do this w/an
      empty sequence.
      Doc: https://clojuredocs.org/clojure.core/str"
      [value]
      (if (= nil value)
        "nil (maybe? empty seq also evals to nil)"
        value
      )
    )
    (defn is-t? [value answer]
      (is
        (= answer (truthy? value))
        (str (pr value) " should be " answer)
        )
      )

    ;; (is
    ;;   (= :truthy true)
    ;;   "true should be truthy"
    ;; )
    (is-t?      true :truthy)
    (is-t? (Object.) :truthy) ; Objects are true
    (is-t?        [] :truthy) ; empty colls are true

    (is-t? false     :falsey)
    (is-t? nil       :falsey)
    (is-t? (seq [])  :falsey)

    (testing "CAVEAT: creating your own booleans is a bad idea!"
      (is-t? Boolean/FALSE :falsey)    ;=> Singleton of java.lang.Boolean
      (def false-god (Boolean. false)) ;=> a new Boolean object that doesn't match the ONE TRUE FALSE
      (is
        (= false false-god)
        "newly created Boolean false objects should equal the false literal"
      )
      (is-t? false-god :truthy) ;=> but will be TRUTHY!! Cue. Face melts
                                ;     It's true from a value perspective because it's an Object
                                ;     and you can see above, Objects are truthy
    )
  )
  (is
    (=
      "2 is even"
      (str "2 is " (if (even? 2) "even" "odd")) ; demonstrates flexibility of flow control expressions
    )                                           ;   they can be inside other expressions
    "2 is even, so this test should say so"
  )
  (is
    (= nil
       (if (true? false) "impossible!")
    )
    "else expression is optional, if expression should return nil if none is provided"
  )
  ;; do expression
  ;;   use to run multiple expressions per-if-branch
  ;; ie.
  ;;   (if (even? 5)
  ;;     (do (println "even")
  ;;         true)
  ;;     (do (println "false")
  ;;         false)
  ;;   )
  ;; ;=> false
  ;; but also prints "odd" string
  ;; seems a bit hard to test tho at this point, not sure how to verify things outside of the expression return....

  ;; if-let expression
  ;;  instead of a do expression, many times you want to use let as the if branch
  ;;  so this combines the two for you
  ;;  Only one binding allowed & that is what's tested for truthiness for flow control purposes
  (is
    (=
      [3]
      (if-let [x (filter odd? [2 3 4])] x)
    )
    "if-let should bind x to the odds out of [2 3 4] (so just [3]), then return that"
  )

  ;; cond expression
  (testing "cond expression"
    (def test-cond-1 5)
    (is
      (=
        :group2
        (cond
          (< test-cond-1  4) :group1
          (< test-cond-1  8) :group2
          (< test-cond-1 12) :group3
        )
      )
      "5 should be in :group2 (grouped by 4s)"
    )
    (def test-cond-2 13)
    (is
      (=
        :group4
        (cond
          (< test-cond-2  4) :group1
          (< test-cond-2  8) :group2
          (< test-cond-2 12) :group3
          :else              :group4
        )
      )
      "13 should be in :group4 (uses optional :else key to cond expression)"
    )
  )
  (testing "condp expression"
    (def test-condp 44)
    (is
      (=
        :group4
        (condp = test-condp ; '= test-condp' is a 'shared predicate'
          4  :group1        ;   to condp expr
          8  :group2
          12 :group3
             :group4 ; default or else option
        )
      )
      "44 should be in :group4 (else case of condp)"
    )
  )
  ;; case expression
  ;;   same as condp, but predicate is always '='
  ;;   test-values must be compile-time literals
  ;;     b/c this becomes a map structure
  ;;   match is O(1)
  ;;     a much faster structure than condp
  ;;   else expr has no test value, just like condp

  ;; clojure provides `loop` to do "classic" recursion
  ;; - closed to consumers, lower-level
  ;; sequences repr iteration as values
  ;; - encourages partial iteration rather than complete

  ;; doseq expression - iterates over a sequence
  ;;   similar to Java's foreach loop
  ;;   if given a lazy sequence, `doseq` forces evaluation
  ;;   returns `nil`, since it's usually used for side-effects
  ;;
  ;; (doseq [n (range 3)]
  ;;   (println n)
  ;; )
  ;; prints 0 1 2 (w/newlines between), then returns `nil`
  ;;
  ;; `doseq` can take multiple bindings
  ;;   similar to nested foreach loops
  ;;   processes all permutations of sequence content

  ;; `dotimes` - evaluate expr `n` times
  ;;
  ;; (dotimes [i 3]
  ;;   (println i)
  ;; )
  ;; prints like `doseq` above, returns `nil` for the same reason as well

  ;; `while` - evaluate expr while condition is true
  ;;
  ;; (while (.accept socket)
  ;;   (handle socket)
  ;; )

  ;; `for` - list comprehension, NOT a for-loop
  ;;
  ;; generator fn for sequence permutation
  ;;
  ;; (for [x [0 1]
  ;;       y [0 1]]
  ;;   [x y]
  ;; )
  ;; ;=> ([0 0] [0 1] [1 0] [1 1]) ;seq

  ;; `loop` - functional looping construct
  ;;
  ;; `loop` defines bindings, just like `let` does
  ;; `recur` is the big difference between the two
  ;; - in a loop, `recur` sets up the next iteration of the loop
  ;; - but it means nothing in a `let` expression
  ;;
  ;; (loop [i 0]
  ;;   (if (< i 10)
  ;;     (recur (inc i))
  ;;     i
  ;;   )
  ;; )
  ;; ;=> 10
  ;; does iterate over all of the previous values, incrementing i until it's 10, then returns it

  ;; `recur` also means someting inside fns
  ;; - that it calls the fn again, w/args reset exactly the same way they do in a loop

  ;; `recur` for Recursion
  ;;
  ;; `recur` must be in "tail position": meaning the last expression in a branch
  ;; `recur` must provide values for all bound symbols by position
  ;; - loop bindings or defn/fn args
  ;; recursion via `recur` doesn't consume stack
  ;; - I'm not sure what this means, something about optimizing away stack frames
  ;; - ah, found out in the demo
  ;;   - using the fn name when recurring charges us the full cost
  ;;     - a new fn stack frame will be added on
  ;;   - but if we're talking about tail recursion, then using recur will unroll our code into a loop
  ;;     - so we don't pay this price & can do much larger things
  (defn factorial
    ([n] (factorial 1 n))
    ([accum n]
     (if (zero? n)
       accum
       (factorial (*' accum n) (dec n)) ; fix: replace `factorial` w/ `recur`
     )
    )
  )
  ;; test: (factorial 5000) ; produces StackOverflowError before, an answer after

  ;; exception handling
  ;;
  ;; example:
  ;; (try
  ;;   (/ 2 1)
  ;;   (catch ArithmeticException e
  ;;     "divide by zero"
  ;;   )
  ;;   (finally
  ;;     (println "cleanup")
  ;;   )
  ;; )
  ;; ; cleanup
  ;; ;=> 2
  ;;
  ;; curious, it seems like there are two returns from a try expression w/a
  ;; finally section? What's up w/that?

  ;; throwing exceptions
  (is
    (=
      :caught
      (try
        (do
          (throw (Exception. "something went wrong"))
          :not-caught
        )
        (catch Exception e
          :caught
        )
      )
    )
    "throw should be caught & return :caught"
  )

  ;; `with-open` expression
  ;;
  ;; in JDK7, Java intro'd `try-with-resources`
  ;; essentially like python's `with` statement:
  ;; - defines the resource in a scope explicitly
  ;; - closes/destroys that resource at the end of the block
  ;;
  ;; clojure uses `with-open` for this same purpose. Example:
  ;;
  ;; (require '[clojure.java.io :as io])
  ;; (with-open [f (io/writer "/tmp/new")]
  ;;   (.write f "some text")
  ;; )

  ;; Flow Control - Summary
  ;;
  ;; flow control constructs permit conditional evaluation of code
  ;; clojure's flow control operators are all /expressions/, not statements
  ;; everything except `false` & `nil` is logically true
  ;; - any object including collections, whether or not they are empty, are logically true
  ;; `if` expects condition, then, & an optional `else` expression
  ;; - `do` can be sued to turn multiple expressions into one
  ;; `cond`, `condp`, & `case` take pairs of either test expressions or values
  ;; `doseq`, `dotimes`, & `while` can be used to iterate for side effects
  ;; clojure's `for` is not a for loop - it's a sequence generator
  ;; `recur` can be used for recursion inside a `loop` or a fn
  ;; - `recur` doesn't consume the runtime stack
  ;; clojure can catch & throw exceptions in a style similar to Java's 
)
