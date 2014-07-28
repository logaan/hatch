(ns admin.ui.components-test
  (:require [om.core :as om]
            [admin.ui.entity :as entity]
            [admin.ui.action :as action]))

(declare hosts-data)
(declare host-data)
(declare action-data)

(defn render! []
  (om/root entity/component hosts-data
           {:target (js/document.getElementById "entity-hosts")})
  (om/root entity/component host-data
           {:target (js/document.getElementById "entity-host")})
  (om/root action/component action-data
           {:target (js/document.getElementById "action")}))

(def hosts-data
  {:class "hosts",
   :entities
   [{:class ["host"],
     :rel ["host"],
     :href "http://localhost:8080/hosts/17592186046789",
     :properties {:db/id 17592186046789, :host/name "aaa"},
     :links
     [{:rel ["self"],
       :href "http://localhost:8080/hosts/17592186046789"}]}
    {:class ["host"],
     :rel ["host"],
     :href "http://localhost:8080/hosts/17592186046796",
     :properties {:db/id 17592186046796, :host/name "bbb"},
     :links
     [{:rel ["self"],
       :href "http://localhost:8080/hosts/17592186046796"}]}],
   :links [{:rel ["self"], :href "http://localhost:8080/hosts"}],
   :actions
   [{:name "add-host",
     :title "Add Host",
     :method "POST",
     :href "http://localhost:8080/hosts",
     :type "application/edn",
     :fields [{:name :host/name, :title "Name", :type :text}]}
    {:name "search-hosts",
     :title "Search Hosts",
     :method "GET",
     :href "http://localhost:8080/hosts",
     :type "application/x-www-form-urlencoded",
     :fields [{:name :query, :title "Query", :type :search}]}]})

(def host-data
  {:class "host",
   :properties {:db/id 17592186046789, :host/name "aaa"},
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

(def action-data
   {:action action
    :form   {}})

(render!)
