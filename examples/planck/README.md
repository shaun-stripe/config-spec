# Planck

Planck is not intended to produce a build like other CLJS tools.  Rather, it is
intended to run ClojureScript code as a shell script.  Still, Planck requires
some build options to be specified.

## Example Planck project

> __NOTE__: requires a snapshot build of Planck 1.12 (for foreign-libs and opts.clj)

If we wish to install JS dependencies for use in Planck, we specify them in package.json
as standard and run:

```
$ npm install
```

`opts.clj` is an early method for Planck to see them:

```clj
;; in opts.clj
{:foreign-libs [{:file "node_modules/marked/lib/marked.js"
                 :provides ["npm.marked"]}]}
```

we can run the following

```sh
$ planck \
    -c src \      # set classpath (location of source and/or jar dependencies)
    -m foo.core   # main namespace
```

## cljs.edn proposal

Planck config could be represented by a `:planck` build in cljs.edn:

```clj
;; in cljs.edn

{:builds
 {:planck {:src "src"
           :compiler {:main foo.core
                      :foreign-libs [{:file "node_modules/marked/lib/marked.js"
                                      :provides ["npm.marked"]}]}}}}
```

Running the project with:

```
$ planck
```

## Maven dependencies

This still leaves us to retrieve and specify Maven dependencies in a separate config
with lein or boot.

```
$ planck -c`lein classpath`
or
$ planck -c`boot show -c`
```
