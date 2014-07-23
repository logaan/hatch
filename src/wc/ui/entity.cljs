(ns wc.ui.entity
  (:require [om.core  :as om  :include-macros true]
            [om.dom   :as dom :include-macros true]
            [uri.core :as uri]))

(defn get-link [rel ent] ;; FIXME
  (->> ent
       :links
       (filter #(some #{rel} (:rel %)))
       first
       :href))

(defn ent->href [ent]
  (or (:href ent)
      (get-link "self" ent)))

(defn ent->fragment [ent]
  (str "#" (uri/relative (ent->href ent))))

(defn action->fragment [ent action]
  (str (ent->fragment ent) "#" (:name action)))

(defn link-to-entity [{:keys [href rel] :as ent}]
  (dom/a #js{:href (ent->fragment ent)} (first rel)))

(defn link-to-action [ent {:keys [title] :as action}]
  (dom/a #js{:href (action->fragment ent action)} title))

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
  (wrap-list "Actions" (map #(link-to-action ent %) (:actions ent))))

(defn display-name [ent]
  (or
   (-> ent :properties :title)
   (let [[[_ v]]
         (filter (fn [[k v]] (= "name" (name k)))
                 (ent :properties))]
     v)))

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
