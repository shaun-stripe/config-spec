(require '[cljs.build.api :as b]
         '[clojure.edn :as edn])

(def proj (edn/read-string (slurp "cljs.edn")))

(let [{:keys [src compiler]} (-> proj :builds :dev)]
  (b/watch src compiler))
