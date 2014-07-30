(ns admin.ui.page-test
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.test-data :as test-data]
            [admin.ui.nav :as nav]
            [admin.ui.entity :as entity]))

(defn page [data owner]
  (om/component
   (dom/div
    nil
    (om/build nav/component    test-data/nav-data)
    (om/build entity/component test-data/ent))))

(defn render! []
  (om/root page {}
           {:target (js/document.getElementById "app")}))

(render!)
