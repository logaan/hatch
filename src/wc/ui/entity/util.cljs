(ns wc.ui.entity.util
  (:require [om.dom     :as dom]
            [uri.core   :as uri]
            [siren.core :as siren]))

(defn ent->href [ent]
  (or (:href ent)
      (siren/get-link ent "self")))

(defn ent->fragment [ent]
  (str "#" (uri/relative (ent->href ent))))

(defn action->fragment [ent action]
  (str (ent->fragment ent) "#" (:name action)))

(defn link-to-entity [{:keys [href rel] :as ent}]
  (dom/a #js{:href (ent->fragment ent)} (first rel)))

(defn link-to-action [ent {:keys [title] :as action}]
  (dom/a #js{:href (action->fragment ent action)} title))
