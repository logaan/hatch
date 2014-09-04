(ns admin.ui.action
  (:require [cljs.reader :as r]
            [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.event :as ev]))

(defn default-input [values id input-type field-key]
  (dom/input
      #js{:id id
          :className "form-control"
          :type (name input-type)
          :name (str field-key)
          :value (values field-key)
          :onChange
          (fn [e]
            (om/update! values field-key (.. e -target -value)))}))

(defn fmt [n]
  (.slice (str "0" n) -2))

(defn datetime-input [values id input-type field-key]
  (dom/div
   #js{:className "input-group"}
   (dom/input
    #js{:id id
        :className "form-control"
        :type "datetime"
        :value (if-let [value (values field-key)]
                 (str (.getFullYear value) "-"
                      (fmt (.getMonth value)) "-"
                      (fmt (.getDay value)) " "
                      (fmt (.getHours value)) ":"
                      (fmt (.getMinutes value))
                      ))
        :name (str field-key)})
   (dom/span #js{:className "input-group-addon"}
             (dom/span #js{:className "glyphicon glyphicon-calendar"}))))

(defn input [values id input-type field-key]
  (condp = input-type
    :datetime (datetime-input values id input-type field-key)
    (default-input values id input-type field-key)))

(defn labeled-input [values {title :title field-key :name input-type :type}]
  (let [id (name (gensym))]
    (dom/div
     #js{:className "form-group"}
     (dom/label
      #js{:className "form-label"
          :htmlFor id}
      title)
     (input values id input-type field-key)
     )))

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
    :else                 (labeled-input values field)))

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
      (field->editable  values (first field-group))
      (fields->editable values        field-group))))

(defn action-form [{:keys [action values on-submit]}]
  (apply
   dom/form
   #js{:role "form"}
   (concat
    (fields->inputs values (:fields action))
    [(dom/button
      #js{:type "submit"
          :className "btn btn-primary"
          :onClick (ev/prevent-default #(on-submit action values))}
                 "Submit")])))

(defn component [data owner]
  (reify
    om/IWillMount
    (will-mount [this]
                (let [fields (get-in data [:action :fields])
                      value-fields (filter :value fields)
                      values (into {} (map (juxt :name :value) value-fields))]
                  (om/transact! (:values data) #(merge % values))
                  ))
    om/IDidMount
    (did-mount [this]
               (let [el (.getDOMNode owner)
                     values (:values data)]
                 (.. (js/$ el)
                     (find "[type=\"datetime\"]")
                     datetimepicker
                     (on "changeDate"
                        (fn [ev]
                          (js/console.log (.-date ev))
                          (om/update! values
                                      (r/read-string (-> ev .-target .-name))
                                      (.-date ev)))))))
    om/IRender
    (render [this]
            (dom/div
             nil
             (dom/h1 nil (get-in data [:action :title]))
             (dom/a #js{:href (:back-href data)} "back")
             (dom/hr nil)
             (action-form data)
             ))))
