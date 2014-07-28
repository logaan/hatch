(ns admin.ui.components-test
  (:require [om.core :as om]
            [admin.ui.entity :as entity]
            [admin.ui.action :as action]))

(def entity
  {:class "host",
   :properties {:db/id 17592186046789, :host/name "Some Lame Host"},
   :links
   [{:rel ["self"], :href "http://localhost:8080/hosts/17592186046789"}
    {:rel ["listing"], :href "http://localhost:8080/hosts"}],
   :actions
   [{:name "delete-host",
     :title "Delete Host",
     :method "DELETE",
     :href "http://localhost:8080/hosts/17592186046789"}
    {:name "replace-host",
     :title "Replace Host",
     :method "PUT",
     :href "http://localhost:8080/hosts/17592186046789",
     :type "application/edn",
     :fields [{:name :host/name, :title "Name", :type :text}]}]})

(def action
  {:name "add-host",
   :title "Add Host",
   :method "POST",
   :href "http://localhost:8080/hosts",
   :type "application/edn",
   :fields [{:name :host/name, :title "Name", :type :text}]})

(def action-form
   {:action action
    :form   {}})

(defn render! []
  (om/root entity/component entity
           {:target (js/document.getElementById "entity")})
  (om/root action/component action-form
           {:target (js/document.getElementById "action")}))

(render!)
