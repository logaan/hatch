(ns admin.ui.test-data)

(defn mk-subent [id name]
  {:class "entity"
   :rel ["some-rel"]
   :href (str "http://localhost:8080/entity/" id)
   :properties {
                :db/id 1
                :ent/name name
                :ent/count 1
                :ent/description "some kind of long property"
                :ent/prop "prop"
                }
   :actions
   [{:name "delete-ent",
     :title "Delete Entity",
     :method "DELETE",
     :href "http://localhost:8080/delete-ent"}
    {:name "replace-ent",
     :title "Replace Entity",
     :method "PUT",
     :href "http://localhost:8080/replace-ent",
     :type "application/edn",
     :fields [{:name :ent/name, :title "Name", :type :text}]}]
   })

(def ent
  {:class ["entity"]
   :entities (for [n (range 1 21)] (mk-subent n (str "Entity " n)))
   :links [{:rel ["self"] :href "http://localhost:8080/entities"}]
   :actions
   [{:name "add-entity",
     :title "Add Entity",
     :method "POST",
     :href "http://localhost:8080/entity",
     :type "application/edn",
     :fields [{:name :ent/name, :title "Name", :type :text}]}
    {:name "delete-entity",
     :title "Search Entities",
     :method "GET",
     :href "http://localhost:8080/entity",
     :type "application/x-www-form-urlencoded",
     :fields [{:name :query, :title "Query", :type :search}]}]
   })

(def hosts-data
  {:class "hosts",
   :entities
   [{:class ["host"],
     :rel ["host"],
     :href "http://localhost:8080/hosts/17592186046789",
     :properties {:db/id 17592186046789, :host/name "Plato's Academy"},
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
       :fields [{:name :host/name, :title "Name", :type :text}]}],
     :links
     [{:rel ["self"],
       :href "http://localhost:8080/hosts/17592186046789"}]}
    {:class ["host"],
     :rel ["host"],
     :href "http://localhost:8080/hosts/17592186046796",
     :properties {:db/id 17592186046796, :host/name "Aristotle's Lyceum"},
     :actions
     [{:name "delete-host",
       :title "Delete Host",
       :method "DELETE",
       :href "http://localhost:8080/hosts/17592186046796"}
      {:name "replace-host",
       :title "Replace Host",
       :method "PUT",
       :href "http://localhost:8080/hosts/17592186046796",
       :type "application/edn",
       :fields [{:name :host/name, :title "Name", :type :text}]}],
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
   :properties {:db/id 17592186046789, :host/name "Robert Norzick School of Liberty"},
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

(def ent-w-props
  {:class "video",
   :properties
   {"name" "2013 Australian Championship",
    "embed_code" "RuY3dybjqjHRhxWynjaO-pmqND7Tc6JX",
    "updated_at" #inst "2014-07-09T19:32:16.000-00:00",
    "status" "live",
    "preview_image_url"
    "http://ak.c.ooyala.com/RnNXFxMjpguay62NBwy6zbh8N_OEfpJi/promo137893580",
    "duration" 63589,
    :db/id "RuY3dybjqjHRhxWynjaO-pmqND7Tc6JX",
    "created_at" #inst "2014-07-09T19:24:08.000-00:00",
    "asset_type" "channel",
    "description" ""},
   :links
   [{:rel ["self"],
     :href
     "http://localhost:8080/videos/RuY3dybjqjHRhxWynjaO-pmqND7Tc6JX"}
    {:rel ["listing"], :href "http://localhost:8080/videos"}],
   :actions
   [{:name "delete-video",
     :title "Delete Video",
     :method "DELETE",
     :href
     "http://localhost:8080/videos/RuY3dybjqjHRhxWynjaO-pmqND7Tc6JX"}
    {:name "replace-video",
     :title "Replace Video",
     :method "PUT",
     :href
     "http://localhost:8080/videos/RuY3dybjqjHRhxWynjaO-pmqND7Tc6JX",
     :type "application/edn",
     :fields [{:name :video/name, :type :text, :title "Name"}]}]})

(def action
  {:name "add-event",
   :title "Add Event",
   :method "POST",
   :href "http://localhost:8080/events",
   :type "application/edn",
   :fields
   [{:name :event/name, :type :text, :title "Name"}
    {:name :event/host,
     :value 17592186046789,
     :type :radio,
     :title "aaa"}
    {:name :event/host,
     :value 17592186046796,
     :type :radio,
     :title "bbb"}]})

(def action-data
   {:action action
    :form   {}})

(def root-ent
  {:class "index",
   :links
   [{:rel ["self"],   :href "http://localhost:8080/"}
    {:rel ["hosts"],  :href "http://localhost:8080/hosts"}
    {:rel ["events"], :href "http://localhost:8080/events"}]})

(def nav-data
  {:title "Webcasting"
   :entity root-ent})
