(ns admin.ui.page-test
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.test-data :as test-data]
            [admin.ui.nav :as nav]
            [admin.ui.entity :as entity]))

(def state
  (atom {:nav test-data/nav-data
         :ent test-data/ent}))

(defn page [data owner]
  (om/component
   (dom/div
    #js{:className "container"}
    (om/build nav/component    (:nav data))
    (om/build entity/component (:ent data)))))

(defn render! []
  (om/root page state
           {:target (js/document.getElementById "app")}))

(render!)
