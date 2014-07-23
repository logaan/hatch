(ns wc.ui.state
  (:require [cljs.reader :as reader]
            [wc.ui.xhr   :as xhr]))

(defonce state (atom {}))

(defn on-entity! [ent]
  (swap! state
    #(-> % (assoc  :entity ent)
           (dissoc :action :form))))

(defn get-link [ent rel]
  (->> ent
       :links
       (filter #(some #{rel} (:rel %)))
       first
       :href))

(defn get-action [ent action-name]
  (->> ent
       :actions
       (filter #(= action-name (:name %)))
       first))

(defn ->edn [xhr]
  (reader/read-string (.getResponseText xhr)))

(declare present!)

(def http-ok         200)
(def http-created    201)
(def http-no-content 204)

(defn on-complete! [xhr e]
  (condp = (.getStatus xhr)
   http-ok         (on-entity! (->edn xhr))
   http-created    (present!   (.getResponseHeader xhr "Location"))
   http-no-content (present!   (get-link (:entity @state) "self"))))

(defn present! [href]
  (xhr/req
   {:method      "GET"
    :url         href
    :on-complete on-complete!}))

(defn show-action-form! [action]
  (swap! state   assoc
         :action action
         :form   {}))

(defn exec-action!
  ([action] ;; TODO should we always go to the listing after form-free actions?
   (xhr/req
    {:method       (:method  action)
     :url          (:href    action)
     :type         (:type    action)
     :on-complete #(present! (get-link (:entity @state) "listing"))}))
  ([action form]
   (xhr/req
    {:method       (:method  action)
     :url          (:href    action)
     :type         (:type    action)
     :data         form
     :on-complete  on-complete!})))

(defn perform-action-named! [name]
  )

(defn perform-action!
  ([action]
   (if (:fields action)
     (show-action-form! action)
     (exec-action!      action)))
  ([action form]
   (exec-action! action form)))

(defn cancel-action! []
  (swap! state dissoc :action :form))
