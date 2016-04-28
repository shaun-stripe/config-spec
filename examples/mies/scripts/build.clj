(require '[cljs.build.api :as b]
         '[clojure.edn :as edn])

(def proj (edn/read-string (slurp "cljs.edn")))

(println "Building ...")

(let [start (System/nanoTime)
      config (-> proj :builds :dev)]
  (b/build (:src config) config)
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
