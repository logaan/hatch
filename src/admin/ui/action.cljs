(ns admin.ui.action
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]))

(defn input [values {title :title field-key :name input-type :type}]
  (let [id (name (gensym))]
    (dom/div
     #js{:className "form-group"}
     (dom/label
      #js{:className "form-label"
          :htmlFor id}
      title)
     (dom/input
      #js{:id id
          :className "form-control"
          :type (name input-type)
          :name (str field-key)
          :onChange
          (fn [e]
            (om/update! values field-key (.. e -target -value)))}))))

(defn radio-input [values {:keys [title value] field-key :name}]
  (dom/div
   #js{:className "radio"}
   (dom/label
    nil
    (dom/input
     #js{:type "radio"
         :name  (str field-key)
         :value (str value)
         :onChange
         (fn [_]
           (om/update! values field-key value))})
    title)))

(defn field->input [values {input-type :type :as field}]
  (cond
    (= input-type :radio) (radio-input values field)
    :else                 (input values field)))

(defn editable [{:keys [values field]} owner]
  (reify
    om/IRender
    (render
     [this]
     (field->input values field))))

(defn field->editable [values field]
  (om/build editable {:values values :field field}))

(defn fields->editable [values field-group]
  (apply
   dom/div
   #js{:className "form-group"}
   (concat
    [(dom/label
      #js{:className "form-label"}
      (-> field-group first :name name))]
    (om/build-all
     editable
     (map (fn [field] {:values values :field field})
          field-group)))))

(defn singleton? [col]
  (and (not (empty? col))
       (empty? (rest col))))

(defn fields->inputs [values fields]
  (for [field-group (partition-by :name fields)]
    (if (singleton? field-group)
      (field->editable values (first field-group))
      (fields->editable values field-group))))

(defn prevent-default [handler]
  (fn default-preventer [ev]
    (.preventDefault ev)
    (handler ev)))

(defn action-form [{:keys [action values on-submit]}]
  (apply
   dom/form
   #js{:role "form"}
   (concat
    (fields->inputs values (:fields action))
    [(dom/button
      #js{:type "submit"
          :className "btn btn-primary"
          :onClick (prevent-default #(on-submit action values))}
                 "Submit")])))

(defn component [data owner]
  (om/component
   (dom/div
    nil
    (dom/h1 nil (get-in data [:action :title]))
    (dom/a #js{:href "javascript:history.back()"} "back")
    (dom/hr nil)
    (action-form data)
    )))
