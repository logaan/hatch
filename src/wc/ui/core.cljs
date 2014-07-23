(ns wc.ui.core
  (:require [om.core     :as om]
            [wc.ui.app   :as app]
            [wc.ui.state :as state]))

(enable-console-print!)

(defn render! []
  (om/root app/component state/state
   {:target (. js/document (getElementById "app"))}))

(app/init!)
(state/present! "/")
(render!)
