(ns admin.ui.components-test
  (:require [om.core :as om]
            [admin.ui.test-data :as test-data]
            [admin.ui.entity :as entity]
            [admin.ui.action :as action]
            [admin.ui.login  :as login]))

(defn render! []
  (om/root login/component
           {:on-submit #(js/alert (pr-str %))}
           {:target (js/document.getElementById "login")})
  (om/root entity/component test-data/hosts-data
           {:target (js/document.getElementById "entity-hosts")})
  (om/root entity/component test-data/ent-w-props
           {:target (js/document.getElementById "entity-host")})
  (om/root action/component test-data/action-data
           {:target (js/document.getElementById "action")}))

(render!)
