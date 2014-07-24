(ns wc.ui.state
  (:require [uri.core      :as uri]
            [siren.core    :as siren]
            [wc.ui.history :as history]
            [wc.ui.xhr     :as xhr]))

(defonce state (atom {}))

(defn ^:private current-entity []
  (:entity @state))

(defn ^:private self []
  (siren/get-link (current-entity) "self"))

(defn ^:private activate-action [state action-name]
  (if-let [action (siren/get-action (:entity state) action-name)]
    (assoc state :action action :form {})
    state))

(defn ^:private activate-action! [action-name]
  (swap! state activate-action action-name))

(declare get-entity!)
(declare on-entity!)

(defn present! [href]
  (let [[base action-name] (uri/split-fragment href)
        data-loaded        (= base (uri/relative (self)))]
    (condp = [(boolean data-loaded) (boolean action-name)]
      [true    true] (activate-action! action-name)
      [true   false] (on-entity! (:entity @state))
      [false   true] (do (swap! state assoc :pending-action action-name)
                         (get-entity! base))
      [false  false] (get-entity! base))))

(defn ^:private activate-pending-action [{:keys [pending-action entity] :as state}]
  (if pending-action
    (-> state
        (activate-action pending-action)
        (dissoc :pending-action))
    state))

(defn on-entity! [ent]
  (swap! state
         #(-> % (assoc  :entity ent)
                (dissoc :action :form)
                activate-pending-action)))

(defn ^:private goto-listing! [xhr e]
  (history/goto! (siren/get-link (current-entity) "listing")))

(defn ^:private reload! [href]
  (history/goto! href))

(defn ^:private on-ok! [xhr]
  (on-entity! (xhr/->edn xhr)))

(defn ^:private on-complete! [xhr e]
  (condp = (xhr/status xhr)
    xhr/ok         (on-ok!  xhr)
    xhr/created    (reload! (xhr/header xhr "Location"))
    xhr/no-content (reload! (self))))

(defn ^:private get-entity! [href]
  (xhr/req
   {:method      "GET"
    :url         href
    :on-complete on-complete!}))

(defn perform-action!
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
