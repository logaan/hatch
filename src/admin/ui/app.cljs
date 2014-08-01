(ns admin.ui.app
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [uri.core         :as uri]
            [admin.xhr        :as xhr]
            [admin.ui.state   :as state]
            [admin.ui.entity  :as entity]
            [admin.ui.action  :as action]
            [admin.ui.history :as history]))

(defn on-response [cursor]
  (fn [res ev]
   (condp = (.getStatus res)
     200 (om/update! cursor :entity (xhr/->edn res)))))

(defn present! [cursor href]
  (js/console.log "Present: " href)
  (xhr/req
   {:method "GET"
    :url href
    :on-complete (on-response cursor)}))

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
