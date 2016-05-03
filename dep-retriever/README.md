# Dependency Retriever

If cljs.edn is to allow a `:dependencies` vector, it might make sense to have
some build-tool agnostic way to retrieve them.  [Planck] and the [CLJS Quick Start]
already rely on either of the following commands for downloading dependencies
and resolving their classpath:

[Planck]:http://planck-repl.org/dependencies.html
[CLJS Quick Start]:https://github.com/clojure/clojurescript/wiki/Quick-Start#dependencies

```
$ lein classpath
$ boot show -c
```

Both tools use a Clojure library `cemerick.pomegranate`, which wraps the Java
library Aether which is the standard interface to Maven repositories.  Knowing
this, we implement a minimal implementation of the commands above with the code
below.

```clj
(ns deps.core
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
       (join ":")))
```

If we bundled this into an uberjar, the minimal prereqs for retrieving cljs
deps will be the JRE and this jar.
