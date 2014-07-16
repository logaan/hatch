(ns wc.ui.core
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [ankha.core :as ankha]
            [wc.ui.xhr :as xhr]))

(enable-console-print!)

(def app-state (atom {}))

(defn on-entity! [ent]
  (swap! app-state assoc :entity ent)
  #_(debug/inspect! @app-state))

(defn present-entity! [href]
  (xhr/req
   {:method :get
    :url href
    :on-complete on-entity!}))

(defn entity-link [{:keys [href rel]}]
  (letfn [(on-click [ev]
            (.preventDefault ev)
            (present-entity! href))]
    (dom/a #js{:href href :onClick on-click} (first rel))))

(defn wrap-list [title ls]
  (when (not (empty? ls))
    (dom/div nil
            (dom/h5 nil title)
            (apply dom/ul nil
                   (map #(dom/li nil %) ls)))))

(defn entities-list [ent]
  (wrap-list "Entities"
             (om/build-all entity (:entities ent))))

(defn links-list [ent]
  (wrap-list "Links"
             (map entity-link (:links ent))))

(defn actions-list [ent]
  (wrap-list "Actions"
             (for [action (:actions ent)]
               (:title action))))

(defn display-name [ent]
  (->> ent
       :properties
       (filter (fn [[k v]] (= "name" (name k))))
       first second))

(defn entity [ent owner]
  (reify
    om/IRender
    (render [this]
      (dom/div
       nil
       (dom/h4 nil (display-name ent))
       (links-list ent)
       (entities-list ent)
       (actions-list ent)
       ))))

(defn app [data owner]
  (reify
    om/IRender
    (render [this]
      (om/build entity (:entity data)))))

(defn render! []
  (om/root app app-state
   {:target (. js/document (getElementById "app"))}))

(present-entity! "/hosts")
(render!)
