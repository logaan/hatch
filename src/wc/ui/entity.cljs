(ns wc.ui.entity
  (:require [om.core    :as om  :include-macros true]
            [om.dom     :as dom :include-macros true]
            [uri.core   :as uri]
            [wc.ui.entity.util :as util]))

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
  (wrap-list "Links" (map util/link-to-entity (:links ent))))

(defn actions-list [ent]
  (wrap-list "Actions" (map #(util/link-to-action ent %) (:actions ent))))

(defn find-key-pred [pred coll]
  (let [[[_ v]] (filter (fn [[k _]] (pred k)) coll)] v))

(defn display-name [ent]
  (or (-> ent :properties :title)
      (find-key-pred #(= "name" (name %)) (:properties ent))))

(defn component [ent owner]
  (reify
    om/IRender
    (render [this]
      (dom/div
       nil
       (dom/h4 nil (display-name ent))
       (links-list    ent)
       (entities-list ent)
       (actions-list  ent)
       ))))
