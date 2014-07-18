(ns debug
  (:require [om.core :as om :include-macros true]
            [ankha.core :as ankha]))

(def inspected (atom nil))

(defn render! []
  (om/root ankha/inspector inspected
   {:target (js/document.getElementById "inspector")}))

(defn inspect! [v]
  (reset! inspected v)
  (render!))

(defn attach-inspector
  ([target]
   (attach-inspector target identity))
  ([target wrap]
    (om/root (fn [data _] (om/build ankha/inspector (wrap data))) target
     {:target (js/document.getElementById "inspector")})))
