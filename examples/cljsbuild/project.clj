(defproject foo "0.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.34"]]
  :plugins [[lein-cljsbuild "1.1.3"]]

  ;; Hypothetical option load cljs.edn
  ;; :cljsbuild {:use-cljs-edn true}

  ;; What the option above would expand to:
  :cljsbuild {:builds
              {:dev {:source-paths ["src"]
                     :compiler {:main foo.core
                                :output-to "out/foo.js"
                                :output-dir "out"
                                :verbose true}}
               :release {:source-paths ["src"]
                         :compiler {:output-to "release/foo.js"
                                    :output-dir "release"
                                    :optimizations :advanced
                                    :verbose true}}}})

;; Below is code that could run if :use-cljs-edn is true
;; which will generate the above builds from cljs.edn

(comment
  (require '[clojure.edn :as edn])
  (def proj (edn/read-string (slurp "cljs.edn")))

  (defn build [{:keys [src compiler] :as b}]
    {:source-paths (if (seq? src) src [src])
     :compiler compiler})

  (defn cljsbuilds [cljs-edn]
    (->> (:builds proj)
         (map (fn [[k v]] [k (build v)]))
         (into {})))

  (cljsbuilds (:builds proj)))
