(ns debug
  (:require [om.core :as om :include-macros true]
            [ankha.core :as ankha]))

(def inspected (atom))

(defn render! []
  (om/root ankha/inspector inspected
   {:target (js/document.getElementById "inspector")}))

(defn inspect! [v]
  (reset! inspected v)
  (render!))
