# Dependency Retriever

If cljs.edn is to allow a `:dependencies` vector, it might make sense to have
some build-tool agnostic way to retrieve them. [Planck] and the [CLJS Quick
Start] already rely on either of the following commands for downloading
dependencies and resolving their classpath:

[Planck]:http://planck-repl.org/dependencies.html
[CLJS Quick Start]:https://github.com/clojure/clojurescript/wiki/Quick-Start#dependencies

```
$ lein classpath
$ boot show -c
```

Both tools use a Clojure library `cemerick.pomegranate`, which wraps the Java
library Aether which is the standard interface to Maven repositories.  Knowing
this, we implement a minimal implementation of the commands above with a small
library.

## Building and Using

```
lein uberjar
```

```
$ ./cljs-install --help

Retrieve dependencies listed in a given .edn file, and print comma-delimited classpath for use with the `java -cp` option.

Argument: your-filename.edn (defaults to cljs.edn)

Options:
  -c, --classpath   Print comma-delimited classpath of dependencies
  -p, --production  Install only :dependencies
  -d, --dev         Install only :dev-dependencies
  -h, --help
```
