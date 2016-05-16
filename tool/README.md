# Tool

Exploring a simple build tool, somewhere between [Quick Start] and [mies].
Not sure if valuable yet.

[Quick Start]:https://github.com/clojure/clojurescript/wiki/Quick-Start
[mies]:https://github.com/swannodette/mies

## Setup

First build the dep-retriever:

```
$ cd dep-retriever
$ lein uberjar
```

Now build the tool itself:

```
$ node tool.js build tool
```

This will override `tool.js`.  If it breaks, use this to restore a working version:

```
$ git checkout -- tool.js
```

## Usage

- `node tool.js install`
- `node tool.js build <id>`

