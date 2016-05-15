(ns std-cljs.core
  (:require
    [clojure.string :as string]
    [util.io :as io]))

;; config data
(def config nil)
(def deps-cache nil)

(def dep-keys [:dependencies
               :dev-dependencies])

;; filenames
(def file-config-edn "cljs.edn")
(def file-config-json "cljs.json")
(def file-deps-cache ".deps-cache.edn")

;;---------------------------------------------------------------------------
;; Misc
;;---------------------------------------------------------------------------

(def child-process (js/require "child_process"))
(def spawn-sync (.-spawnSync child-process))
(def exec (.-exec child-process))

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

(defn ensure-dependencies!
  (let [cache (io/slurp-edn file-deps-cache)
        stale? (not= (select-keys config dep-keys)
                     (select-keys cache dep-keys))]
    (if stale?
      (task-install)
      cache)))

;;---------------------------------------------------------------------------
;; Main Tasks
;;---------------------------------------------------------------------------

(defn task-install []
  (ensure-java!)
  (let [jar (str js/__dirname "/dep-retriever.jar")
        deps (apply concat (map config dep-keys))
        result (spawn-sync "java"
                 #js["-jar" jar (pr-str deps-str)]
                 #js{:stdio #js["pipe" ;; stdin (captured)
                                "pipe" ;; stdout (captured)
                                2]}) ;; stderr (printed)
        success? (and (zero? (.-status result))
                      (not (.-error result)))]
    (when success?
      (let [cache (-> config
                      (select-keys dep-keys)
                      (assoc :jars (string/split (.-stdout result) "\n")))]
        (io/spit file-deps-cache (pr-str cache))
        cache))))

(defn build-classpath [{:keys [src] :as build}]
  (let [jars (string/split (ensure-classpath!) ":")
        source-paths (if (sequential? src) src [src])
        all (concat jars source-paths)]
    (string/join ":" all)))

(defn task-build [id]
  (ensure-java!)
  (ensure-dependencies!)
  (let [build (ensure-build! id)]
    (spawn-sync "java"
      #js["-cp"
          (build-classpath build)
          "clojure.main"
          (str js/__dirname "/build.clj")
          (pr-str build)]
      #js{:stdio "inherit"})))

(defn custom-script [id]
  (exec-sync (ensure-cmd! id) #js{:stdio "inherit"}))

(defn -main [task id]
  (set! config (ensure-config!))
  (cond
    (= task "install") (task-install)
    (= task "build") (task-build id)
    :else (custom-script task)))

(set! *main-cli-fn* -main)
(enable-console-print!)
