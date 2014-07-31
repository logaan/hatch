(ns admin.ui.core
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.nav :as nav]
            [admin.ui.app :as app]
            [admin.ui.test-data :as test-data]
            [admin.xhr :as xhr]
            ))

(defonce state (atom nil))

(reset!
 state
 {:nav {}
  :app {:entity test-data/hosts-data}
  })

(defn page [data owner]
  (om/component
   (dom/div
    #js{:className "container"}
    (om/build nav/component (:nav data))
    (om/build app/component (:app data)))))

(defn render! []
  #_(debug/attach-inspector state
   #(update-in % [:app] assoc :entity "..."))
  (om/root page state
   {:target (js/document.getElementById "app")}))

(render!)

(comment
 (xhr/req
  {:method "GET"
   :url "/"
   :on-complete
   (fn [xhr e]
     (js/console.log
      (.getResponseText xhr)))}))
