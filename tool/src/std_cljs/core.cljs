(ns std-cljs.core
  (:require
    [clojure.string :as string]
    [util.io :as io]))

;; config data
(def config nil)

;; filenames
(def file-config-edn "cljs.edn")
(def file-config-json "cljs.json")
(def file-classpath ".classpath")
(def file-deps-cache ".deps-cache.edn")

;;---------------------------------------------------------------------------
;; Misc
;;---------------------------------------------------------------------------

(def child-process (js/require "child_process"))
(def spawn-sync (.-spawnSync child-process))
(def exec (.-exec child-process))

(defn run-cmd [cmd]
  (let [p (exec cmd)]
    (-> p .-stdout (.pipe js/process.stdout))
    (-> p .-stderr (.pipe js/process.stderr))))

(defn exit-error [& args]
  (apply js/console.error args)
  (js/process.exit 1))

;;---------------------------------------------------------------------------
;; Validation
;;---------------------------------------------------------------------------

(defn java-installed? []
  (not (.-error (spawn-sync "java"))))

(defn ensure-java! []
  (or (java-installed?)
      (exit-error "Please install Java.")))

(defn ensure-config! []
  (or (io/slurp-edn config-edn)
      (io/slurp-json config-json)
      (exit-error "No config found. Please create one in" config-edn "or" config-json)))

(defn ensure-cmd! [id]
  (or (get-in config [:scripts (keyword id)])
      (exit-error (str "Unrecognized command: '" id "' is not found in :scripts map"))))

(defn ensure-build! [id]
  (or (get-in config [:builds (keyword id)])
      (exit-error (str "Unrecognized build: '" id "' is not found in :builds map"))))

(declare task-install)

(defn ensure-classpath! []
  (or (io/slurp file-classpath)
      (do (task-install) (io/slurp file-classpath))
      (exit-error "Dependency tool failed to build classpath.")))

(defn ensure-dependencies!
  (let [curr (select-keys config [:dependencies :dev-dependencies])
        prev (io/slurp-edn file-deps-cache)
        stale? (not= curr prev)]
    (when stale?
      (task-install)
      (spit file-deps-cache (pr-str curr)))))

;;---------------------------------------------------------------------------
;; Main Tasks
;;---------------------------------------------------------------------------

(defn task-install []
  (ensure-java!)
  (run-cmd (str "java -jar " js/__dirname "/dep-retriever.jar")))

(defn build-classpath [{:keys [src] :as build}]
  (let [jars (string/split (ensure-classpath!) ":")
        source-paths (if (sequential? src) src [src])
        all (concat jars source-paths)]
    (string/join ":" all)))

(defn task-build [id]
  (ensure-java!)
  (ensure-dependencies!)
  (let [build (ensure-build! id)]
    (run-cmd
      (string/join
        ["java"
         "-cp" (build-classpath build)
         "clojure.main"
         (str js/__dirname "/build.clj")
         (str \' (pr-str build) \')]))))

(defn custom-script [id]
  (run-cmd (ensure-cmd! id)))

(defn -main [task id]
  (set! config (ensure-config!))
  (cond
    (= task "install") (task-install)
    (= task "build") (task-build id)
    :else (custom-script task)))

(set! *main-cli-fn* -main)
(enable-console-print!)
