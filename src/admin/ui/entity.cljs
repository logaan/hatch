(ns admin.ui.entity
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.entity.util :as util]))

(defn subentity-row [props ent]
  (apply
   dom/tr
   nil
   (map #(dom/td nil %)
    (concat
     (for [prop props
           :let [value ((:properties ent) prop)]]
       (if (util/name-prop? prop)
         (util/ent->a ent value)
         value))
     [(subactions ent)]))))

(defn thead [props]
  (dom/thead
   nil
   (apply
    dom/tr
    nil
    (concat
     (for [prop props]
       (dom/th nil (name prop)))
     [(dom/th nil)]))))

(defn subent-table [ents]
  (let [props (util/union-props ents)]
    (dom/table
     #js{:className "table table-striped"}
     (thead props)
     (apply
      dom/tbody
      nil
      (map #(subentity-row props %) ents)))))

(defn subentities [ent]
  (let [ents (:entities ent)]
    (when (not (empty? ents))
      (subent-table ents))))

(defn links-list [ent]
  (let [links (util/non-self-links ent)]
    (util/wrap-list "links"
     (map util/link->a links))))

(defn actions [ent]
  (apply dom/div
         #js{:className "actions"}
         (map #(util/action->button ent %)
              (:actions ent))))

(defn subactions [ent]
  (apply dom/div
         #js{:className "subactions"}
         (map #(util/subaction->button ent %)
              (:actions ent))))

(defn title [ent]
  (dom/h1 #js{:className "title"}
          (util/display-name ent)))

(defn component [ent owner]
  (om/component
   (dom/div
    nil
    (title       ent)
    (actions     ent)
    (dom/hr nil)
    (subentities ent)
    (links-list  ent)
    )))
