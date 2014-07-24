(ns wc.ui.state
  (:require [uri.core      :as uri]
            [siren.core    :as siren]
            [wc.ui.history :as history]
            [wc.ui.xhr     :as xhr]))

(defonce state (atom {}))

; --

(defn on-entity! [ent]
  (swap! state assoc :entity ent))

(defn on-ok [xhr]
  (on-entity! (xhr/->edn xhr)))

(defn on-complete [xhr e]
  (condp = (xhr/status xhr)
    xhr/ok (on-ok xhr)))

(defn get-entity [href]
  (xhr/req
   {:method      "GET"
    :url         href
    :on-complete on-complete}))

; --

(defn present! [href]
  (get-entity href))

(defn perform-action! [action form])
