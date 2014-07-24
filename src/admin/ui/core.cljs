(ns admin.ui.core
  (:require [om.core      :as om]
            [admin.ui.app :as app]))

(defn render! []
  (debug/attach-inspector app/state #(dissoc % :entity))
  (om/root app/component app/state
   {:target (js/document.getElementById "app")}))

(app/init!)
(render!)
