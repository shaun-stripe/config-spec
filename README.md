# Standardized CLJS - Draft

Can the following CLJS build tools share a common project config file/format?

- [cljs.jar] - direct interface to the compiler
- [lein-cljsbuild] - historical de facto
- [lein-figwheel] - reloadable builder repl
- [boot-cljs] - cljs compiler as a middleware task
- [planck] - repl/build/run cljs as OSX shell script
- [LightTable] - editor auto-builds cljs files on save
- [cuttle] - graphical build window

## Why try standardizing?

At the moment, we have a bunch of really useful build tools for different
domains.  It wouldn't make sense to brand one as the "standard", but project
configuration between them seems common enough to start brainstorming a shared
format.  It may help to simplify the overall CLJS environment we are creating
for ourselves, especially for newcomers.

## Current State

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

## Shared Config Proposal - cljs.edn

A plain data file called `cljs.edn` can represent general cljs project
metadata.  Any tool can use it to analyze the project's dependencies and build
configs.  Fields are optional since some build tools may only care about a
particular subset.

```clj
;; filename: cljs.edn

{:name "ProjectName"
 :version "0.0.0"

 :dependencies [[org.clojure/clojurescript "1.8.40"]
                [hiccups "0.3.0"]
                ...]

 :builds {:build-id {...compiler options}
          ...}}
```

_NOTE: This is similar to the `defproject` lein macro, but it is not
complicated by the necessity to merge in profile data, and it contains no info
about specific build tools._

Integration with lein/boot might be done with some plugins:

- __lein-cljs-edn__ - a plugin for adding info from cljs.edn to project.clj
- __boot-cljs-edn__ - a plugin for adding info from cljs.edn to build.boot (and generate per-build edn files in the fileset?)
- __lint-cljs-edn__ - common validator against a spec?

There won't be libraries necessary for scripts that just want to read the data
of course.  Planck won't need everything in there, but can use it to
read/retrieve dependencies and for special compiler options it needs.

## Feedback

Nothing here is final.  Feedback appreciated!  Discussions can take place under Issues.

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
