(ns wc.ui.entity
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [wc.ui.state :as state]))

(defn link-to-entity [{:keys [href rel]}]
  (letfn [(on-click [ev]
            (.preventDefault ev)
            (state/present! href))]
    (dom/a #js{:href href :onClick on-click} (first rel))))

(defn link-to-action [{:keys [title] :as action}]
  (letfn [(on-click [ev]
            (.preventDefault ev)
            (state/perform-action! @action))]
    (dom/a #js{:href "#" :onClick on-click} title)))

(defn wrap-list [title ls]
  (when (not (empty? ls))
    (dom/div nil
            (dom/h5 nil title)
            (apply dom/ul nil
                   (map #(dom/li nil %) ls)))))

(declare component)

(defn entities-list [ent]
  (wrap-list "Entities" (om/build-all component (:entities ent))))

(defn links-list [ent]
  (wrap-list "Links" (map link-to-entity (:links ent))))

(defn actions-list [ent]
  (wrap-list "Actions" (map link-to-action (:actions ent))))

(defn display-name [ent]
  (->> ent
       :properties
       (filter (fn [[k v]] (= "name" (name k))))
       first second))

(defn component [ent owner]
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
