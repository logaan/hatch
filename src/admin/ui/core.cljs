(ns admin.ui.core
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.nav :as nav]
            [admin.ui.app :as app]
            ))

(defonce state (atom nil))

(reset!
 state
 {:nav {:title "Webcasting"}
  :app {:auth {}} ;; FIXME how do I get rid of this?
  })

(defn page [data owner]
  (om/component
   (dom/div
    #js{:className "container"}
    (om/build nav/component {:data (:nav data)
                             :auth (:auth data)})
    (om/build app/component (:app data)))))

(defn render! []
  (debug/attach-inspector state
   #(-> %
        (debug/clear [:app :entity])
        (debug/clear [:app :current-request])
        (debug/clear [:nav :entity])
        ))
  (om/root page state
   {:target (js/document.getElementById "app")}))

(render!)
