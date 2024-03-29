(defproject clojure-noob "0.1.0-SNAPSHOT"
  :description "Project follows me learning Clojure through various sources"
  :url "https://github.com/kvimber/clojure-noob"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
                 [org.clojure/clojure "1.10.1"]
                 [com.clojure-goes-fast/clj-async-profiler "1.0.2"]
                 [clj-time "0.15.2"]]
  :main ^:skip-aot clojure-noob.cftbat.ch1
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
