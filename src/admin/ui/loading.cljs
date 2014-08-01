(ns admin.ui.loading
  (:require [om.core :as om]))

(defn begin-loading! [cursor]
  (om/update! cursor :loading true))

(defn finish-loading! [cursor]
  (om/update! cursor :loading false))

(defn loading? [cursor]
  (:loading @cursor))
