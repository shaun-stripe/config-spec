(defproject foo "0.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.34"]]
  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.1"]]

  ;; Hypothetical option load cljs.edn
  ;;:cljsbuild {:use-cljs-edn true}

  ;; What the option above would expand to:
  :cljsbuild {:builds
              {:dev {:source-paths ["src"]
                     :figwheel true
                     :compiler {:main foo.core
                                :asset-path "js/out"
                                :output-to "resources/public/js/foo.js"
                                :output-dir "resources/public/js/out"
                                :optimizations :none
                                :verbose true}}
               :release {:source-paths ["src"]
                         :compiler {:asset-path "js/release"
                                    :output-to "resources/public/js/foo.min.js"
                                    :output-dir "resources/public/js/release"
                                    :optimizations :advanced
                                    :verbose true}}}})

;; Below is code that could run if :use-cljs-edn is true
;; which will generate the above builds from cljs.edn

(comment
  (require '[clojure.edn :as edn])
  (def proj (edn/read-string (slurp "cljs.edn")))

  (defn build [{:keys [src figwheel compiler] :as b}]
    (cond-> {:source-paths (if (seq? src) src [src])
             :compiler compiler}
     figwheel (assoc :figwheel figwheel)))

  (defn cljsbuilds [cljs-edn]
    (->> (:builds proj)
         (map (fn [[k v]] [k (build v)]))
         (into {})))

  (cljsbuilds (:builds proj)))
