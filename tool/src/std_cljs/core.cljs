(ns std-cljs.core
  (:require
    [util.io :as io]))

(def exec (aget (js/require "child_process") "exec"))

(defn get-config []
  (cond
    (io/path-exists? "cljs.edn") (io/slurp-edn "cljs.edn")
    (io/path-exists? "cljs.json") (io/slurp-json "cljs.json")
    :else nil))

(defn run-cmd [cmd]
  (let [process (exec cmd)]
    (-> process .-stdout (.pipe js/process.stdout))
    (-> process .-stderr (.pipe js/process.stderr))))

(defn install []
  (run-cmd "java -jar dep-retriever.jar"))

(defn custom-script [config task]
  (when-let [cmd (get-in config [:scripts (keyword task)])]
    (run-cmd cmd)
    (println "Unrecognized command:" task)))

(defn -main [task]
  (if-let [config (get-config)]
    (cond
      (= task "install") (install)
      :else (custom-script config task))
    (println "No config found. Please create a cljs.edn or cljs.json file.")))

(set! *main-cli-fn* -main)
(enable-console-print!)
