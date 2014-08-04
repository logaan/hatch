(ns admin.ui.entity
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.entity.subent :as subent]
            [admin.ui.entity.util :as util]))

(defn title [ent]
  (dom/h1 #js{:className "title"}
          (util/display-name ent)))

(defn actions [ent]
  (apply
   dom/div
   #js{:className "actions"}
   (map #(util/action->button ent %)
        (:actions ent))))

(defn key->str [k]
  (.replace (name k) (js/RegExp. "_" "g") " "))

(defn prop->tr [[k v]]
  (dom/tr
   nil
   (dom/td #js{:className "property-name"}  (key->str k))
   (dom/td #js{:className "property-value"} (str v))))

(defn properties [{props :properties}]
  (when props
    (dom/table
     #js{:className "table table-striped properties"}
     (apply
      dom/tbody
      nil
      (map prop->tr props)))))

(defn links-list [ent]
  (let [links (util/non-self-links ent)]
    (util/wrap-list "links"
     (map util/link->a links))))

(defn component [ent owner]
  (om/component
   (dom/div
    nil
    (title        ent)
    (actions      ent)
    (dom/hr nil)
    (properties   ent)
    (subent/table ent)
    (links-list   ent)
    )))
