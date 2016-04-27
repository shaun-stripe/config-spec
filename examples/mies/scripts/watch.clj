(require '[cljs.build.api :as b]
         '[clojure.edn :as edn])

(def proj (edn/read-string (slurp "cljs.edn")))

(b/watch "src"
  (-> proj :builds :dev))
