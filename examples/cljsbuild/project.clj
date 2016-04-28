(defproject foo "0.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.34"]]
  :plugins [[lein-cljsbuild "1.1.3"]]

  ;; Hypothetical option to expand to the config below.
  ;; :cljsbuild {:use-cljs-edn true}

  :cljsbuild {:builds
              {:dev {:source-paths ["src"]
                     :compiler {:src "src"
                                :main foo.core
                                :output-to "out/foo.js"
                                :output-dir "out"
                                :verbose true}}
               :release {:source-paths ["src"]
                         :compiler {:src "src"
                                    :output-to "release/foo.js"
                                    :output-dir "release"
                                    :optimizations :advanced
                                    :verbose true}}}})

;; Below is code that could run if :use-cljs-edn is true
;; which will generate the above builds from cljs.edn

(comment
  (defn build [{:keys [src] :as b}]
    {:source-paths (if (seq? src) src [src])
     :compiler b})

  (defn cljsbuilds [cljs-edn]
    (->> (:builds proj)
         (map (fn [[k v]] [k (build v)]))
         (into {})))

  (require '[clojure.edn :as edn])
  (def proj (edn/read-string (slurp "cljs.edn")))
  (cljsbuilds (:builds proj)))
