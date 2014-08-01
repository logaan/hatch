(ns admin.ui.nav
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [siren.core :as siren]
            [admin.xhr :as xhr]
            [admin.ui.entity.util :as util]))

(defn load-root-entity [on-entity]
  (xhr/req
   {:method "GET"
    :url "/"
    :on-complete
    (fn [xhr e]
      (on-entity (xhr/->edn xhr)))}))

(defn nav-items [links]
  (dom/div
   #js{:className "navbar-collapse collapse"}
   (apply dom/ul
          #js{:className "nav navbar-nav"}
          (for [link links]
            (dom/li
             #js{:className "" #_"active"}
             (util/link->a link))))))

(defn collapse-button []
  (dom/button
   #js{:type "button"
       :className "navbar-toggle"
       :data-toggle "collapse"
       :data-target ".navbar-collapse"}
   (dom/span #js{:className "sr-only"})
   (dom/span #js{:className "icon-bar"})
   (dom/span #js{:className "icon-bar"})
   (dom/span #js{:className "icon-bar"})))

(defn brand [title href]
  (dom/a #js{:className "navbar-brand" :href href} title))

(defn component [{:keys [title entity loader] :as data} owner]
  (reify
    om/IWillMount
    (will-mount [this]
     (when loader
       (om/update! data :loading true)
       (letfn [(on-entity [ent]
                 (om/update! data :entity ent)
                 (om/update! data :loading false))]
         (loader on-entity))))

    om/IRender
    (render [this]
      (dom/div
       #js{:className "navbar navbar-default"
           :role "navigation"}
       (dom/div
        #js{:className "container-fluid"}
        (dom/div
         #js{:className "navbar-header"}
         (brand title (util/->fragment entity))
         (collapse-button))
        (nav-items (util/non-self-links entity))
        )))))
