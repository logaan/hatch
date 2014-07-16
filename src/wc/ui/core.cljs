(ns wc.ui.core
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [ankha.core :as ankha]
            [wc.ui.xhr :as xhr]))

(enable-console-print!)

(def app-state (atom {}))

(defn entity [ent owner]
  (reify
    om/IRender
    (render [this]
      (dom/div
       nil
       (dom/h4 nil (-> ent :properties :host/name)
       (apply dom/ul nil
              (om/build-all entity (:entities ent))))))))

(defn app [data owner]
  (reify
    om/IRender
    (render [this]
      (om/build entity (:data data)))))

(defn render! []
  (om/root app app-state
   {:target (. js/document (getElementById "app"))}))

(defn load-data [data]
  (swap! app-state assoc :data data)
  (debug/inspect! @app-state))

(xhr/req
 {:method :get
  :url "/hosts"
  :on-complete load-data})

(render!)
