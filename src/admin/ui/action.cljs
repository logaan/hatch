(ns admin.ui.action
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.state :as state]))

(defn on-change [form field-key]
  (fn [e] (om/transact! form field-key (fn [_] (.. e -target -value)))))

(defn editable [{form :form {field-key :name} :field} owner]
  (reify
    om/IRender
    (render [this]
      (dom/div
       nil
       (dom/label nil (str (name field-key) ": "))
       (dom/input
        #js{:type "text"
            :onChange
            (on-change form field-key)}))
      )))

(defn field->editable [form field]
  (om/build editable {:form form :field field}))

(defn on-submit
  [action form]
   (fn [ev]
     (.preventDefault ev)
     (state/perform-action! @action @form)))

(defn action-form [{:keys [action form]}]
  (apply dom/form nil
    (concat
      (for [field (:fields action)]
        (field->editable form field))
      [(dom/input #js{:type "submit"
                      :value "Submit"
                      :onClick (on-submit action form)})])))

(defn component [data owner]
  (reify
    om/IRender
    (render [this]
      (dom/div nil
        (dom/h1 nil (get-in data [:action :title]))
        (action-form data)
        ))))
