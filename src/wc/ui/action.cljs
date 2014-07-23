(ns wc.ui.action
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [wc.ui.state  :as state]
            [wc.ui.entity :as entity]
            [wc.ui.xhr    :as xhr]))

(defn editable [{:keys [form field-key]} owner]
  (reify
    om/IRender
    (render [this]
      (dom/div nil
        (dom/label nil (str (name field-key) ": "))
        (dom/input
         #js{:type "text"
             :onBlur
             (fn [e]
               (om/transact! form field-key (fn [_] (.. e -target -value))))}))
    )))

(defn field->editable [form {field-key :name field-type :type}]
  (om/build editable {:form form :field-key field-key}))

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
