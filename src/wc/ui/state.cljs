(ns wc.ui.state
  (:require [cljs.reader   :as reader]
            [uri.core      :as uri]
            [wc.ui.history :as history]
            [wc.ui.xhr     :as xhr]))

(defonce state (atom {}))

(defn get-action [ent action-name]
  (->> ent
       :actions
       (filter #(= action-name (:name %)))
       first))

(defn on-entity! [ent]
  (swap! state
         (fn [state]
           (let [state (assoc state :entity ent)]
             (if-let [pending-action (:pending-action state)]
               (let [action (get-action ent pending-action)]
                 (if (not (:fields action))
                   (do (exec-action! action)
                     (dissoc state :pending-action))
                   (-> state
                       (assoc
                         :action action
                         :form {})
                       (dissoc :pending-action))))
               (dissoc state :action :form))))))


(defn get-link [ent rel]
  (->> ent
       :links
       (filter #(some #{rel} (:rel %)))
       first
       :href))

(defn ->edn [xhr]
  (reader/read-string (.getResponseText xhr)))

(defn load! [xhr]
  (history/replace! (uri/relative (.getLastUri xhr)))
  (on-entity!       (->edn xhr)))

(def http-ok         200)
(def http-created    201)
(def http-no-content 204)

(defn on-complete! [xhr e]
  (condp = (.getStatus xhr)
    http-ok         (load! xhr)
    http-created    (history/goto! (.getResponseHeader xhr "Location"))
    http-no-content (history/goto! (get-link (:entity @state) "self"))))

(defn present! [href]
  (xhr/req
   {:method      "GET"
    :url         href
    :on-complete on-complete!}))

(defn show-action-form! [action]
  (swap! state assoc
         :action action
         :form   {}))

(defn exec-action!
  ([action] ;; TODO should we always go to the listing after form-free actions?
   (xhr/req
    {:method       (:method  action)
     :url          (:href    action)
     :type         (:type    action)
     :on-complete #(history/goto! (get-link (:entity @state) "listing"))}))
  ([action form]
   (xhr/req
    {:method       (:method  action)
     :url          (:href    action)
     :type         (:type    action)
     :data         form
     :on-complete  on-complete!})))

(defn perform-action-named! [name]
  (swap! state assoc :pending-action name))

(defn perform-action!
  ([action]
   (if (:fields action)
     (show-action-form! action)
     (exec-action!      action)))
  ([action form]
   (exec-action! action form)))

(defn cancel-action! []
  (swap! state dissoc :action :form))
