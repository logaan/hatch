(ns wc.ui.core
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [wc.ui.state   :as state]
            [wc.ui.entity  :as entity]
            [wc.ui.action  :as action]
            [wc.ui.xhr     :as xhr]))

(enable-console-print!)

(defn app [data owner]
  (reify
    om/IRender
    (render [this]
      (cond
        (:action data) (om/build action/component (select-keys data [:action :form]))
        (:entity data) (om/build entity/component (:entity data))
        :else          (dom/div nil)))))

(defn render! []
  (om/root app state/state
   {:target (. js/document (getElementById "app"))}))

(state/present! "/hosts")
(render!)
