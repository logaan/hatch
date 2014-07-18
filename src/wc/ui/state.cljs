(ns wc.ui.state
  (:require [wc.ui.xhr :as xhr]))

(def state (atom {}))

(defn on-entity! [ent]
  (swap! state
    #(-> % (assoc  :entity ent)
           (dissoc :action :form))))

(defn present! [href]
  (xhr/req
   {:method      "GET"
    :url         href
    :on-complete on-entity!}))

(defn on-response! [xhr e]
  (fn [xhr e]
    (present!
     (.getResponseHeader xhr "Location"))))

(defn show-action-form! [action]
  (swap! state   assoc
         :action action
         :form   {}))

(defn exec-action! [action]
  (xhr/req
   {:method       (:method  action)
    :url          (:href    action)
    :on-complete #(present! "/hosts")}))

(defn perform-action! [action]
  (if (:fields action)
    (show-action-form! action)
    (exec-action!      action)))

(defn cancel-action! []
  (swap! state dissoc :action :form))
