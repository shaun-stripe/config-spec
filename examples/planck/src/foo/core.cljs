(ns foo.core
  (:require [npm.marked]))

(defn -main []
  (println (js/marked "# Hello World")))
