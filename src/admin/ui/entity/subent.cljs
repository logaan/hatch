(ns admin.ui.entity.subent
 (:require [om.core :as om  :include-macros true]
           [om.dom  :as dom :include-macros true]
           [admin.ui.entity.util :as util]))

(defn subactions [ent]
  (apply
   dom/div
   #js{:className "subactions"}
   (map #(util/subaction->button ent %)
        (:actions ent))))

(defn subent-row [props ent]
  (apply
   dom/tr
   nil
   (map #(dom/td nil %)
    (concat
     (for [prop props
           :let [value ((:properties ent) prop)]]
       (if (util/name-prop? prop)
         (util/ent->a ent value)
         (str value)))
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

(defn tbody [props ents]
  (apply
   dom/tbody
   nil
   (map #(subent-row props %) ents)))

(defn subent-table [ents]
  (let [props (util/union-props ents)]
    (dom/table
     #js{:className "table table-striped"}
     (thead props)
     (tbody props ents))))

(defn table [ent]
  (let [ents (:entities ent)]
    (when (not (empty? ents))
      (subent-table ents))))
