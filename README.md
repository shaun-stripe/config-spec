> __NOTE__: I'm still thinking about this.

# Standardized CLJS - Draft

Standardizing ClojureScript project configuration for a simpler environment.

## Problem

In ClojureScript, we understand the value of storing things in a central place
as plain data.  And yet, we wrap what should be common project configuration in
special interfaces that differ across different build tools:

- [cljs.jar] relies on either lein or boot for defining dependencies, and defines project config inline.
- [lein-cljsbuild] uses its own project config standard, and uses lein for defining dependencies
- [lein-figwheel] piggiebacks on lein-cljsbuild's config standard, but provides a sidecar library for defining project config inline
- [boot-cljs] uses its own project config standard across different files, and uses boot for defining dependencies
- [LightTable] does something that I don't know (TODO)
- [cuttle] piggiebacks on lein-cljsbuild's config

## Proposal - common config

Define the project information as _plain data_ in a canonical file `cljs.edn`.

### Builds

The build tools can be flagged to read build config from this data rather than
through specific interfaces.

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

Feel free to add tool-specific config inside a build, such as `:figwheel true`.
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
