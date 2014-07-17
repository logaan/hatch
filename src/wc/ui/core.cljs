(ns wc.ui.core
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [wc.ui.state :as state]
            [wc.ui.entity :as entity]
            [wc.ui.xhr :as xhr]))

(enable-console-print!)

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
      {:method (:method @action)
       :url (:href @action)
       :data @form
       :on-response (fn [xhr e] (entity/present! (.getResponseHeader xhr "Location")))})))

(defn action-form [{act :action form :form}]
  (apply dom/form nil
    (concat
      (for [field (:fields act)]
        (field->editable form field))
      [(dom/input #js{:type "submit"
                      :value "Submit"
                      :onClick (on-submit act form)})])))

(defn action [data owner]
  (reify
    om/IRender
    (render [this]
      (dom/div nil
        (dom/h1 nil (get-in data [:action :title]))
        (dom/a #js{:href "#" :onClick state/cancel-action!} "back")
        (action-form data)
        ))))

(defn app [data owner]
  (reify
    om/IRender
    (render [this]
      (cond
        (:action data) (om/build action (select-keys data [:action :form]))
        (:entity data) (om/build entity/component (:entity data))
        :else          (dom/div nil)))))

(defn render! []
  ;(debug/attach-inspector state/state)
  (om/root app state/state
   {:target (. js/document (getElementById "app"))}))

(entity/present! "/hosts")
(render!)
