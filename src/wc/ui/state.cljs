(ns wc.ui.state
  (:require [cljs.reader :as reader]
            [wc.ui.xhr :as xhr]))

(defonce state (atom {}))

(defn on-entity! [ent]
  (swap! state
    #(-> % (assoc  :entity ent)
           (dissoc :action :form))))

(def http-ok         200)
(def http-created    201)
(def http-no-content 204)

(defn on-response! [xhr e]
  (condp = (.getStatus xhr)
   http-ok         (on-entity! (reader/read-string (.getResponseText xhr)))
   http-created    (present!   (.getResponseHeader xhr "Location"))
   http-no-content (present!   (get-link "self" (:entity @state)))))

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
  ([action] ;; TODO should we always go to the listing after form-free actions?
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
