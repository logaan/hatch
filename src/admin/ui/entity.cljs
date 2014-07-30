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
    (subent/table ent)
    (links-list   ent)
    )))
