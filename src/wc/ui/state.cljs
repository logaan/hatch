(ns wc.ui.state
  (:require [uri.core      :as uri]
            [siren.core    :as siren]
            [wc.ui.history :as history]
            [wc.ui.xhr     :as xhr]))

(defonce state (atom {}))


(declare perform-pending-action)

(defn on-entity! [ent]
  (swap! state
         #(-> %
              (assoc  :entity ent)
              (dissoc :action ent)
              perform-pending-action)))

(defn perform-action-named! [name]
  (swap! state assoc :pending-action name))

(defn cancel-action! []
  (swap! state dissoc :action :form))

(defn current-entity []
  (:entity @state))


(defn show-action-form [state action]
  (assoc state
         :action action
         :form   {}))


(declare exec-action!)

(defn perform-pending-action [state]
  (let [{ent  :entity
         pend :pending-action} state
        state (dissoc state :pending-action)]
    (if-let [action (siren/get-action ent pend)]
      (if (:fields action)
         (show-action-form state action)
         (do (exec-action! action)
             state))
      state)))


(defn load! [xhr]
  (history/replace! (uri/relative (.getLastUri xhr))) ;; FIXME
  (on-entity!       (xhr/->edn xhr)))

(defn on-complete! [xhr e]
  (condp = (xhr/status xhr)
    xhr/ok         (load! xhr)
    xhr/created    (history/goto! (xhr/header xhr "Location"))
    xhr/no-content (history/goto! (siren/get-link (current-entity) "self"))))

(defn goto-listing! [xhr e]
  (history/goto! (siren/get-link (current-entity) "listing")))

(defn present! [href]
  (xhr/req
   {:method      "GET"
    :url         href
    :on-complete on-complete!}))

(defn exec-action!
  ([action] ;; TODO should we always go to the listing after form-free actions?
   (xhr/req
    {:method       (:method  action)
     :url          (:href    action)
     :type         (:type    action)
     :on-complete  goto-listing!}))
  ([action form]
   (xhr/req
    {:method       (:method  action)
     :url          (:href    action)
     :type         (:type    action)
     :data         form
     :on-complete  on-complete!})))
