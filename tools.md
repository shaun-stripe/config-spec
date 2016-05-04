## Tools

### [cljs.jar]

The ClojureScript compiler is a Clojure library with a public API, which you
can learn to use from the [Quick Start tutorial][cljs.jar].  All build tools make use of
this API.

- [Compiler API docs]
- [Compiler Option docs]

| How to...        | Process | Config File |
|------------------|---------|-------------|
| Retrieve Deps    | manual  | N/A         |
| Configure Builds | manual  | N/A         |

### [lein-cljsbuild]

Builds are defined in `:cljsbuild :builds` path in the `defproject` macro inside `project.clj`.

| How to...        | Process                   | Config File |
|------------------|---------------------------|-------------|
| Retrieve Deps    | lein `:dependencies`      | project.clj |
| Configure Builds | lein `:cljsbuild :builds` | project.clj |

### [lein-figwheel]

Shares lein-cljsbuild's config, but config can alternatively be specified in
`:figwheel` key of `project.clj` (I think), or even inline with `:all-builds`
in a [figwheel-sidecar] script.

| How to...              | Process                        | Config File |
|------------------------|--------------------------------|-------------|
| Retrieve Deps          | lein `:dependencies`           | project.clj |
| Configure Builds       | lein `:cljsbuild :builds`      | project.clj |
|                        | lein `:figwheel :builds`       | project.clj |
|                        | figwheel-sidecar `:all-builds` | (custom)    |

### [boot-cljs]

> _If you're unfamiliar with Boot, read this [excellent intro][boot-intro].
boot-cljs allows you to build cljs, and [boot-reload] is like
figwheel._

Builds are unusually defined by separate
[`<build-id>.cljs.edn`][boot-cljs-builds] files, which represent dynamically
generated [`:main`] namespaces as build entry points.  An implicit one is
generated if none are found.

[boot-intro]:http://www.flyingmachinestudios.com/programming/boot-clj/
[`:main`]:https://github.com/clojure/clojurescript/wiki/Compiler-Options#main
[boot-cljs-builds]:https://github.com/adzerk-oss/boot-cljs/wiki/Usage#multiple-builds

| How to...        | Process                    | Config File                              |
|------------------|----------------------------|------------------------------------------|
| Retrieve Deps    | boot `:dependencies`       | build.boot                               |
| Configure Builds | multiple boot-cljs configs | `<build-id>.cljs.edn` files in classpath |

### [planck]

Compiler options weren't necessary here until there came a desire to use JS
libraries, which meant having to support [`:foreign-libs`] option.  [This might
require] a `-o / --options` option that can name a "compiler options" file.

[This might require]:https://github.com/mfikes/planck/issues/121#issuecomment-213877398
[`:foreign-libs`]:https://github.com/clojure/clojurescript/wiki/Compiler-Options#foreign-libs

| How to...        | Process                    | Config File           |
|------------------|----------------------------|-----------------------|
| Retrieve Deps    | manual                     | N/A                   |
| Configure Builds | `-o / --options` (future)  | file specified in arg |

### [LightTable]

?? builds after files are saved by using [this module][LightTable-build]?

### [cuttle]

We currently just a UI over lein-cljsbuild, so that only allows us to build
lein-cljsbuild projects.


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
