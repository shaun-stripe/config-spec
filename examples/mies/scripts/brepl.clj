(require
  '[cljs.build.api :as b]
  '[clojure.edn :as edn]
  '[cljs.repl :as repl]
  '[cljs.repl.browser :as browser])

(def proj (edn/read-string (slurp "cljs.edn")))

(b/build "src"
  (-> proj :builds :dev))

(repl/repl (browser/repl-env)
  :output-dir (-> proj :builds :dev :output-dir))
