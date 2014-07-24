(ns admin.ui.core
  (:require [om.core     :as om]
            [admin.ui.app   :as app]
            [admin.ui.state :as state]))

(enable-console-print!)

(defn render! []
  (debug/attach-inspector state/state #(dissoc % :entity))
  (om/root app/component state/state
   {:target (js/document.getElementById "app")}))

(app/init!)
(render!)
