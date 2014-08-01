(ns admin.ui.app
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [uri.core         :as uri]
            [siren.core       :as siren]
            [admin.xhr        :as xhr]
            [admin.ui.loading :as loading]
            [admin.ui.state   :as state]
            [admin.ui.entity  :as entity]
            [admin.ui.action  :as action]
            [admin.ui.history :as history]))

(declare present!)
(declare on-entity-ok)

(def http-ok         200)
(def http-created    201)
(def http-no-content 204)

(defn on-response! [cursor res]
  (condp = (.getStatus res)
    http-ok         (on-entity-ok cursor (xhr/->edn res))
    http-created    (history/goto! (.getResponseHeader res "Location"))
    http-no-content (history/goto! (siren/self (:entity @cursor)))
    ))

(defn exec-action!
  ([cursor action]
   (xhr/req
    {:method (:method action)
     :url    (:href action)
     :on-complete
     (fn [res ev] (on-response! cursor res))}))
  ([cursor action values]
   (xhr/req
    {:method (:method action)
     :url    (:href action)
     :data   (pr-str values)
     :headers #js{"Content-Type" "application/edn"}
     :on-complete
     (fn [res ev] (on-response! cursor res))})))

(defn show-action-form [cursor act]
  (om/update! cursor :form
              {:action act
               :values {}
               :back-href (str "#" (:entity-url @cursor))
               :on-submit
               (fn [action values]
                 (js/console.log (clj->js @values))
                 (exec-action! cursor @action @values))}))

(defn set-pending-action [cursor act-name]
  (om/update! cursor :pending-action act-name))

(defn do-pending-action [cursor]
  (when-let [act (siren/get-action
                  (:entity @cursor)
                  (:pending-action @cursor))]
    (if (:fields act)
      (show-action-form cursor act)
      (exec-action! cursor act)))
  (om/update! cursor :pending-action nil))

(defn clear-current-action [cursor]
  (om/update! cursor :action nil)
  (om/update! cursor :form   nil))

(defn on-entity-ok [cursor ent]
  (let [self (uri/relative (siren/self ent))]
    (when (not= self (:entity-url @cursor))
      (om/update! cursor :entity-url self)
      (history/goto! self)))
  (om/update! cursor :entity ent)
  (clear-current-action cursor)
  (do-pending-action cursor))

(defn get-entity [cursor href]
  (loading/begin-loading! cursor)
  (xhr/req
   {:method "GET"
    :url href
    :on-complete
    (fn [res ev]
      (when (> (.getStatus res) 0)
        (om/update! cursor :current-request nil)
        (loading/finish-loading! cursor)
        (on-response! cursor res)))}))

(defn request-entity [cursor href]
  (when-let [req (:current-request @cursor)]
    (.abort req))
  (let [req (get-entity cursor href)]
    (om/update! cursor :current-request req)))

(defn load-entity [cursor href]
  (when (not= href (:entity-url @cursor))
    (om/update! cursor :entity-url href)
    (request-entity cursor href))
  (when (not (loading/loading? cursor))
    (on-entity-ok cursor (:entity @cursor))))

(defn present! [cursor href]
  (js/console.log "Present: " href)
  (let [[base frag] (uri/split-fragment href)]
    (set-pending-action cursor frag)
    (load-entity cursor base)
    ))

(defn component [data owner]
  (reify
    om/IDidMount
    (did-mount [this]
      (history/watch
       (fn [token]
         (present! data (if (= token "") "/" token)))))

    om/IRender
    (render [this]
      (cond
        (:form   data) (om/build action/component (:form   data))
        (:entity data) (om/build entity/component (:entity data))
        :else          (dom/div nil)))))
