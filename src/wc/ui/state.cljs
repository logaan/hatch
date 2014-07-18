(ns wc.ui.state
  (:require [wc.ui.xhr :as xhr]))

(def state (atom {}))

(defn on-entity! [ent]
  (swap! state
    #(-> % (assoc :entity ent)
           (dissoc :action :form))))

(defn present! [href]
  (xhr/req
   {:method "GET"
    :url href
    :on-complete on-entity!}))

(defn on-response! [xhr e]
  (fn [xhr e]
    (present!
     (.getResponseHeader xhr "Location"))))

(defn perform-action! [action]
  (swap! state assoc
         :action @action
         :form {}))

(defn cancel-action! []
  (swap! state dissoc :action :form))
