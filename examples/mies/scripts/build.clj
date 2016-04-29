(require '[cljs.build.api :as b]
         '[clojure.edn :as edn])

(def proj (edn/read-string (slurp "cljs.edn")))

(println "Building ...")

(let [start (System/nanoTime)
      {:keys [src compiler]} (-> proj :builds :dev)]
  (b/build src compiler)
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
