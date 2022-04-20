(ns clojure-noob.pluralsight.fundamentals1
  (:gen-class))

(defn current-work
  "Current working issue, saved as the main call for when I return"
  [& args]
  (println "pluralsight.fundamentals1.-main: nothing doing"))

;; demo: variadic fns & apply relationship
(defn messenger [greeting & who]
  (str greeting who))

(defn messenger2 [greeting & who]
  (apply str greeting " " who))

;; messenger-builder shows how closures capture data from their surrounding context
(defn messenger-builder [greeting]
  (fn [who] (str greeting " " who))) ; closes over greeting

;; ;; greeting provided here, then goes out of scope
;; (def hello-er (messenger-builder "Hello"))
;; 
;; ;; greeting still available b/c hello-er is closure
;; (hello-er "world!")
;; ;; Hello world!
