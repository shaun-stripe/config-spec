(require '[cljs.build.api :as b]
         '[clojure.edn :as edn])

(def proj (edn/read-string (slurp "cljs.edn")))

(let [config (-> proj :builds :dev)]
  (b/watch (:src config) config))
