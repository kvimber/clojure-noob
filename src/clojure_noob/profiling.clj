(ns clojure-noob.profiling)
;; This is the example code from the clj-async-profiler tutorial[1].
;;
;; To run it, run `lein repl` & put these into the prompt:
;; user=> (require 'clojure-noob.profiling)
;; user=> (require '[clj-async-profiler :as prof])
;; user=> (prof/profile (clojure-noob.profiling/main 20))
;; user=> (prof/serve-ui 8080)
;;
;; Some further reading on comparing flamegraphs[2].
;;
;; Sources:
;; 1. Basic usage: Step-by-step tutorial
;;   - http://clojure-goes-fast.com/kb/profiling/clj-async-profiler/basic-usage/
;; 2. clj-async-profiler 0.4.0: diffgraphs and UI
;;   - http://clojure-goes-fast.com/blog/clj-async-profiler-040/

(deftype TreeNode [left right ^int item])

(defn bottom-up-tree [item depth]
 (let [int-item (int item)
       int-depth (int depth)]
   (if (zero? int-depth)
     (TreeNode. nil nil (int item))
     (TreeNode. (bottom-up-tree (dec (* item 2)) (dec depth))
                (bottom-up-tree (* item 2) (dec depth))
                (int item)))))

(defn item-check [^TreeNode node]
  (if (nil? (.left node))
    (.item node)
    (+ (.item node)
       (item-check (.left node))
       (- (item-check (.right node))))))

(defn iterate-trees [mx mn d]
  (let [iterations (bit-shift-left 1 (+ mx mn (- d)))]
    (format "%d\t trees of depth %d\t check: %d" (* 2 iterations) d
            (reduce + (map (fn [i]
                             (+ (item-check (bottom-up-tree i d))
                                (item-check (bottom-up-tree (- i) d))))
                           (range 1 (inc iterations)))))))

(defn main [max-depth]
  (let [min-depth 4
        stretch-depth (inc max-depth)]
    (let [tree (bottom-up-tree 0 stretch-depth)
          check (item-check tree)]
      (printf "stretch tree of depth %d\t check: %d\n" stretch-depth check))
    (let [long-lived-tree (bottom-up-tree 0 max-depth) ]
      (doseq [trees-nfo (map (fn [d]
                               (iterate-trees max-depth min-depth d))
                             (range min-depth stretch-depth 2)) ]
        (println trees-nfo))
      (printf "long lived tree of depth %d\t check: %d\n"
              max-depth (item-check long-lived-tree)))))
