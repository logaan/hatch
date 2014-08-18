(ns admin.ui.nav
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [siren.core :as siren]
            [admin.xhr :as xhr]
            [admin.ui.loading :as loading]
            [admin.ui.login :as login]
            [admin.ui.entity.util :as util]))

(defn logout-button [auth]
  (if (login/logged-in? auth)
    (dom/ul
     #js{:className "nav navbar-nav navbar-right"}
     (dom/li
      nil
      (dom/a
       #js{:href "#"
           :onClick (fn [ev]
                      (.preventDefault ev)
                      (login/logout! auth))}
       "logout")))))

(defn nav-items [links auth]
  (dom/div
   #js{:className "navbar-collapse collapse"}
   (apply dom/ul
          #js{:className "nav navbar-nav"}
          (for [link links]
            (dom/li
             #js{:className "" #_"active"}
             (util/link->a link))))
   (logout-button auth)))

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

(defn load-entity [url on-entity]
  (xhr/req
   {:method "GET"
    :url url
    :on-complete
    (fn [xhr e]
      (on-entity (xhr/->edn xhr)))}))

(defn component [{{:keys [title entity] :as data} :data auth :auth} owner]
  (reify
    om/IWillMount
    (will-mount [this]
     (loading/begin-loading! data)
     (letfn [(on-entity [ent]
              (om/update! data :entity ent)
              (loading/finish-loading! data))]
       (load-entity "/" on-entity)))

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
        (nav-items (util/non-self-links entity) auth)
        )))))
