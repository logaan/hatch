(ns admin.ui.core
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.nav :as nav]
            [admin.ui.app :as app]
            [admin.ui.test-data :as test-data]
            ))

(defonce state
  (atom
   {:nav
    {:title {:label "Webcasting" :href "#"}
     :items [{:label "Hosts"  :href "#/hosts" :active true}
             {:label "Events" :href "#/events"}]}
    :app {:entity test-data/hosts-data}
    }))

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
