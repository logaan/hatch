(ns admin.ui.core
  (:require [om.core      :as om]
            [admin.ui.app :as app]))

(defonce state (atom {}))

(defn render! []
  (debug/attach-inspector app/state #(dissoc % :entity))
  (om/root app/component state
   {:target (js/document.getElementById "app")}))

(app/init!)
(render!)
