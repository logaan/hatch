(ns admin.ui.entity.util
  (:require [clojure.set :as set]
            [om.dom      :as dom]
            [uri.core    :as uri]
            [siren.core  :as siren]))

(defn find-key-pred [pred coll]
  (let [[[_ v]] (filter (fn [[k _]] (pred k)) coll)] v))

(defn name-prop? [prop]
  (= (name prop) "name"))

(defn display-name [ent]
  (or (find-key-pred name-prop? (:properties ent))
      (let [class (:class ent)]
        (if (string? class) class (first class)))))

(defn ent->href [ent]
  (or (:href ent)
      (siren/get-link ent "self")))

(defn ent->fragment [ent]
  (str "#" (uri/relative (ent->href ent))))

(defn action->fragment [ent action]
  (str (ent->fragment ent) "#" (:name action)))

(defn ent->a [ent body]
  (dom/a #js{:href (ent->fragment ent)} body))

(defn link->a [link]
  (dom/a link (-> link :rel first)))

(defn action->a [ent {:keys [title] :as action}]
  (dom/a #js{:href (action->fragment ent action)} title))

(defn action->button [ent {:keys [title] :as action}]
  (dom/a #js{:className "btn btn-default action"
             :href (action->fragment ent action)} title))

(defn subaction->button [ent {:keys [title] :as action}]
  (dom/a #js{:className "btn btn-xs btn-default action subaction"
             :href (action->fragment ent action)} title))

(defn non-self-links [ent]
  (filter #(not (some #{"self"} (:rel %))) (:links ent)))

(defn wrap-list [className ls]
  (when (not (empty? ls))
    (apply dom/ul #js{:className className}
           (map #(dom/li nil %) ls))))

(defn union-props [entities]
  (-> (reduce
       set/union
       (map (comp set keys :properties) entities))
      (disj :db/id)))
