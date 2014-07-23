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
              (dissoc :action :form)
              perform-pending-action!)))

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

(defn perform-pending-action! [state]
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
  (let [ent (xhr/->edn xhr)
        url (uri/relative (siren/get-link ent "self"))]
   (swap! state assoc :current-url url)
   (history/replace! url)
   (on-entity! ent)))

(defn reload! [href]
  (swap! state assoc :reload true)
  (history/goto! href))

(defn on-complete! [xhr e]
  (swap! state dissoc :abort-request :reload)
  (condp = (xhr/status xhr)
    xhr/ok         (load!    xhr)
    xhr/created    (reload! (xhr/header xhr "Location"))
    xhr/no-content (reload! (siren/get-link (current-entity) "self"))))

(defn get! [href]
  (xhr/req
   {:method      "GET"
    :url         href
    :on-complete on-complete!}))

(defn present! [href]
  (if (or (not= (:current-url @state)
                (uri/defragment href))
          (:reload @state))
    (let [req (get! href)]
      (swap! state assoc
             :current-url href
             :abort-request #(xhr/abort req)))
    (on-entity! (:entity @state))))

(defn goto-listing! [xhr e]
  (history/goto! (siren/get-link (current-entity) "listing")))

(defn exec-action!
  ([action]
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
