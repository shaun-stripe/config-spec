(ns deps.retrieve
  (:require
    [clojure.string :as string]
    [cemerick.pomegranate.aether :as aether]))

(def repos
  (merge aether/maven-central
         {"clojars" "http://clojars.org/repo"}))

(defn retrieve [coords]
  (->> (aether/resolve-dependencies :coordinates coords :repositories repos)
       (aether/dependency-files)
       (map str)
       (string/join ":")))
