(ns admin.ui.history
  (:require [uri.core :as uri]))

(defn location-hash []
  (.slice js/window.location.hash 1))

(defn watch [f]
  (letfn [(onhashchange [_] (f (location-hash)))]
    (.addEventListener
     js/window
     "hashchange"
     onhashchange
     false)
    (js/setTimeout onhashchange 0)))

(defn goto! [loc]
  (aset js/window.location "hash" (uri/relative loc)))
