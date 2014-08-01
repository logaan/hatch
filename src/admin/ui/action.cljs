(ns admin.ui.action
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]))

(defn on-change [values field-key]
  (fn [e] (om/update! values field-key (.. e -target -value))))

(defn editable [{values :values {field-key :name} :field} owner]
  (reify
    om/IRender
    (render [this]
      (dom/div
       nil
       (dom/label nil (str (name field-key) ": "))
       (dom/input
        #js{:type "text"
            :onChange
            (on-change values field-key)}))
      )))

(defn field->editable [values field]
  (om/build editable {:values values :field field}))

(defn on-submit
  [action values]
   (fn [ev]
     (.preventDefault ev)
     ;; TODO something useful here
     ))

(defn action-form [{:keys [action values]}]
  (apply
   dom/form
   nil
   (concat
    (for [field (:fields action)]
      (field->editable values field))
    [(dom/button #js{:type "submit"
                     :className "btn btn-primary"
                     :onClick (on-submit action values)}
                 "Submit")])))

(defn component [data owner]
  (om/component
   (dom/div
    nil
    (dom/h1 nil (get-in data [:action :title]))
    (action-form data)
    )))
