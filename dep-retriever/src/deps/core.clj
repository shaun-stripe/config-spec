(ns deps.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as string]
    [clojure.tools.cli :refer [parse-opts]]
    [deps.retrieve :refer [retrieve]])
  (:gen-class))

(def default-edn-filename "cljs.edn")

(def cli-options
  [["-c" "--classpath"  "Print comma-delimited classpath of dependencies"]
   ["-p" "--production" "Install only :dependencies"]
   ["-d" "--dev"        "Install only :dev-dependencies"]
   ["-h" "--help"]])

(defn usage [options-summary]
  (string/join \newline
    [""
     "Retrieve dependencies listed in a given .edn file, and print comma-delimited classpath for use with the `java -cp` option."
     ""
     "Argument: your-filename.edn (defaults to cljs.edn)"
     ""
     "Options:"
     options-summary
     ""]))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        production? (:production options)
        dev? (:dev options)
        filename (or (first arguments) default-edn-filename)]
    (cond
      (:help options)        (exit 0 (usage summary))
      (and production? dev?) (exit 1 (usage summary))
      errors                 (exit 1 (error-msg errors)))
    (let [data (edn/read-string (slurp filename))
          deps (:dependencies data)
          devdeps (:dev-dependencies data)
          coords (cond
                   production? deps
                   dev? devdeps
                   :else (concat deps devdeps))
          classpath (retrieve coords)]
      (when (:classpath options)
        (println classpath)))))
