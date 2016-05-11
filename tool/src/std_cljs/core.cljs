(ns std-cljs.core
  (:require
    [util.io :as io]))

(def child-process (js/require "child_process"))
(def spawn-sync (aget child-process "spawnSync"))
(def exec (aget child-process "exec"))

(defn get-config []
  (cond
    (io/path-exists? "cljs.edn") (io/slurp-edn "cljs.edn")
    (io/path-exists? "cljs.json") (io/slurp-json "cljs.json")
    :else nil))

(defn run-cmd [cmd]
  (let [p (exec cmd)]
    (-> p .-stdout (.pipe js/process.stdout))
    (-> p .-stderr (.pipe js/process.stderr))))

(defn java-installed? []
  (let [error (aget (spawn-sync "java") "error")]
    (not error)))

(defn install []
  (if (java-installed?)
    (run-cmd (str "java -jar " js/__dirname "/dep-retriever.jar"))
    (println "Please install Java.")))

(defn custom-script [config task]
  (if-let [cmd (get-in config [:scripts (keyword task)])]
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
