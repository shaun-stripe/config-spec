> __NOTE__: I'm still thinking about this.

# Standardized CLJS - Draft

Standardizing ClojureScript project configuration for a simpler environment.

## Problem

In ClojureScript, we understand the value of storing things in a central
place as plain data.  And yet, we wrap what should be common project configuration
in special interfaces that differ across different build tools:

- [cljs.jar] relies on either lein or boot for defining dependencies, and defines project config inline.
- [lein-cljsbuild] uses its own project config standard, and uses lein for defining dependencies
- [lein-figwheel] piggiebacks on lein-cljsbuild's config standard, but provides a sidecar library for defining project config inline
- [boot-cljs] uses its own project config standard across different files, and uses boot for defining dependencies
- [LightTable] does something that I don't know (TODO)
- [cuttle] piggiebacks on lein-cljsbuild's config

## Proposal - cljs.edn


### Builds

Define the project information as _plain data_ in a canonical file `cljs.edn`.
The build tools can be flagged to read from this data rather than through
specific interfaces.

```clj
;; filename: cljs.edn

{:builds {:build-id {:src "src"
                     :compiler {:optimizations :none
                                :main foo.core
                                ...}
                     ...}
          ...}

 ...}
```

See the [examples/](examples) directory for how each build tool would make use
of this data.

### Dependencies

Dependency information is also data that should be readable by build tools:

```clj
;; filename: cljs.edn

{:dependencies [[org.clojure/clojurescript "1.8.40"]
                [hiccups "0.3.0"]
                ...]

 :dev-dependencies [[figwheel-sidecar "0.5.0-SNAPSHOT"]
                    ...]

 ...}
```

## Proposal - cljs command

> Experimental

Going further, we could potentially provide a `cljs` command to provide basic
functionalities expected by a cljs user.

### Dependencies

Since all cljs tools currently use the same dependency retrieval code to
install jars to the same `.m2` maven directory, we can do this ourselves (see
demo at [dep-retriever](dep-retriever)):

```
$ cljs install
```

We can further customize install location a `:local-repo` key as lein/boot do,
as well as specifying repository sources with `:repositories` key.

### Publishing

...

```clj
;; filename: cljs.edn

{:name "group/projectname"
 :version "0.1.0"

 ...}
```

```
$ cljs publish clojars
```

### Scripts

In npm, many tools are managed under a single interface by defining a
[scripts](https://docs.npmjs.com/misc/scripts) map.  We could potentially do
the same here:

```clj
;; filename: cljs.edn

{:scripts {:start "boot cljs -watch"
           :figwheel "rlwrap lein figwheel"
           :test "lein doo"
           :repl "planck"
           ...}

 ...}
```

```
$ cljs start
$ cljs figwheel
$ cljs test
$ cljs repl
```

[cljs.jar]:https://github.com/clojure/clojurescript/wiki/Quick-Start
[cljs compiler API]:https://github.com/cljsinfo/cljs-api-docs/blob/catalog/refs/compiler.md
[compiler options]:https://github.com/clojure/clojurescript/wiki/Compiler-Options
[lein-cljsbuild]:https://github.com/emezeske/lein-cljsbuild
[lein-figwheel]:https://github.com/bhauman/lein-figwheel
[figwheel-sidecar]:https://github.com/cljsinfo/cljs-api-docs/blob/catalog/refs/compiler.md
[boot-cljs]:https://github.com/adzerk-oss/boot-cljs
[boot-reload]:https://github.com/adzerk-oss/boot-reload
[planck]:https://github.com/mfikes/planck
[LightTable]:https://github.com/LightTable/LightTable
[LightTable-build]:https://github.com/LightTable/Clojure/blob/master/lein-light-nrepl/src/lighttable/nrepl/cljs.clj
[cuttle]:https://github.com/oakmac/cuttle
[Compiler API docs]:https://github.com/cljsinfo/cljs-api-docs/blob/catalog/refs/compiler.md
[Compiler Option docs]:https://github.com/clojure/clojurescript/wiki/Compiler-Options
[mies]:https://github.com/swannodette/mies
