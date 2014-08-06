(ns admin.ui.entity.util
  (:require [clojure.set    :as set]
            [om.dom         :as dom]
            [uri.core       :as uri]
            [siren.core     :as siren]
            [admin.ui.event :as ev]))

(defn find-key [pred coll]
  (let [[[_ v]] (filter (fn [[k _]] (pred k)) coll)] v))

(defn name-prop? [prop]
  (= (name prop) "name"))

(defn ->class [ent]
  (let [class (:class ent)]
    (if (string? class) class (first class))))

(defn display-name [ent]
  (or (find-key name-prop? (:properties ent))
      (->class ent)))

(defn ->href [ent]
  (uri/relative
   (or (:href ent)
       (siren/self ent))))

(defn ->fragment [ent]
  (str "#" (->href ent)))

(defn action->href [ent act]
  (str (->href ent) "#" (:name act)))

(defn action->fragment [ent act]
  (str "#" (action->href ent act)))

(defn ent->a [ent body]
  (dom/a #js{:href (->fragment ent)} body))

(defn link->a [link]
  (dom/a #js{:href (->fragment link)}
         (-> link :rel first)))

(defn action->class [act]
  (condp = (:method act)
    "POST"   "btn-success"
    "DELETE" "btn-danger"
    "btn-default"))

(defn action->button [ent {:keys [title on-exec] :as act}]
  (dom/a #js{:className (str "action btn "
                             (action->class act))
             :href (action->fragment ent act)
             :onClick (ev/prevent-default on-exec)} title))

(defn subaction->button [ent {:keys [title on-exec] :as act}]
  (dom/a #js{:className (str "action subaction btn btn-xs "
                             (action->class act))
             :href (action->fragment ent act)
             :onClick (ev/prevent-default on-exec)} title))

(defn non-self-links [ent]
  (filter #(not (some #{"self"} (:rel %)))
          (:links ent)))

(defn wrap-list [className ls]
  (when (not (empty? ls))
    (apply dom/ul #js{:className className}
           (map #(dom/li nil %) ls))))

(defn union-props [entities]
  (-> (reduce
       set/union
       (map (comp set keys :properties) entities))
      (disj :db/id)))
