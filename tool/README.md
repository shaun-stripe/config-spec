# Tool

> __NOTE__: This is an experiment to learn about cljs building process.

A minimal ClojureScript build tool that could use `cljs.edn` or
`package-cljs.json` config file (a la _npm_).  It provides a layer over the
[Quick Start] scripts to provide dependency management.

```
./cljs install
./cljs build <id>
./cljs watch <id>
./cljs repl [<id>]
./cljs <script_id>
```

[Quick Start]:https://github.com/clojure/clojurescript/wiki/Quick-Start

## Implementation

- `src/` - top-level tool implemented in ClojureScript on Node.js
- `dep-retriever/` - minimal java tool for downloading dependencies
- `script/` - clojure "scripts" for accessing cljs compiler


## Setup

Install some prerequisites:

```
$ npm install
$ pushd dep-retriever; lein uberjar; popd
```

And build the tool (it builds itself):

```
$ ./cljs build tool
```

This will override `target/tool.js`.  If it breaks, use this to restore a working version:

```
$ git checkout target/tool.js
```
