(ns wc.ui.action
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [wc.ui.state  :as state]
            [wc.ui.entity :as entity]
            [wc.ui.xhr    :as xhr]))

(defn editable [{:keys [form field]} owner]
  (reify
    om/IRender
    (render [this]
      (dom/div nil
        (dom/input
         #js{:type "text"
             :onChange
             (fn [e]
               (om/transact! form field (fn [_] (.. e -target -value))))}))
    )))

(defn field->editable [form {field-name :name field-type :type}]
  (om/build editable {:form form :field field-name}))

(defn on-submit
  [action form]
   (fn [ev]
     (.preventDefault ev)
     (xhr/req
      {:method      (:method @action)
       :url         (:href @action)
       :data        @form
       :on-response state/on-response!})))

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
        (dom/a #js{:href "#" :onClick state/cancel-action!} "back")
        (action-form data)
        ))))
