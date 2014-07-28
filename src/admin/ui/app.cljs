(ns admin.ui.app
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [uri.core         :as uri]
            [admin.ui.state   :as state]
            [admin.ui.entity  :as entity]
            [admin.ui.action  :as action]
            [admin.ui.history :as history]))

(defn present! [cursor href]
  (js/console.log "Present: " href))

(defn component [data owner]
  (reify
    om/IDidMount
    (did-mount [this]
      (history/watch
       (fn [token]
         (present! data (if (= token "") "/" token)))))

    om/IRender
    (render [this]
      (cond
        (:action data) (om/build action/component (select-keys data [:action :form]))
        (:entity data) (om/build entity/component (:entity data))
        :else          (dom/div nil)))))
