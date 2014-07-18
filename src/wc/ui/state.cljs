(ns wc.ui.state
  (:require [cljs.reader :as reader]
            [wc.ui.xhr :as xhr]))

(defonce state (atom {}))

(defn on-entity! [ent]
  (swap! state
    #(-> % (assoc  :entity ent)
           (dissoc :action :form))))

(defn on-response! [xhr e]
  (condp = (.getStatus xhr)
   200 (on-entity! (reader/read-string (.getResponseText xhr)))
   201 (present!   (.getResponseHeader xhr "Location"))
   204 (present!   (get-link "self" (:entity @state)))))

(defn present! [href]
  (xhr/req
   {:method      "GET"
    :url         href
    :on-response on-response!}))

(defn show-action-form! [action]
  (swap! state   assoc
         :action action
         :form   {}))

(defn get-link [rel ent]
  (->> ent
       :links
       (filter #(some #{rel} (:rel %)))
       first
       :href))

(defn exec-action!
  ([action]
   (xhr/req
    {:method       (:method  action)
     :url          (:href    action)
     :on-complete #(present! (get-link "listing" (:entity @state)))}))
  ([action form]
   (xhr/req
    {:method       (:method  action)
     :url          (:href    action)
     :data         form
     :on-response  on-response!})))

(defn perform-action!
  ([action]
   (if (:fields action)
     (show-action-form! action)
     (exec-action!      action)))
  ([action form]
   (exec-action! action form)))

(defn cancel-action! []
  (swap! state dissoc :action :form))
