(ns admin.ui.page-test
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.components-test :as comp-test]
            [admin.ui.entity :as entity]))

(defn nav-items [items]
  (dom/div
   #js{:className "navbar-collapse collapse"}
   (apply dom/ul
          #js{:className "nav navbar-nav"}
          (for [{:keys [label href active]} items]
            (dom/li
             #js{:className (if active "active")}
             (dom/a #js{:href href} label))))))

(defn nav-collapse-button []
  (dom/button
   #js{:type "button"
       :className "navbar-toggle"
       :data-toggle "collapse"
       :data-target ".navbar-collapse"}
   (dom/span #js{:className "sr-only"})
   (dom/span #js{:className "icon-bar"})
   (dom/span #js{:className "icon-bar"})
   (dom/span #js{:className "icon-bar"})))

(defn brand [{:keys [label href]}]
  (dom/a #js{:className "navbar-brand" :href href} label))

(defn nav [data owner]
  (om/component
   (dom/div
    #js{:className "navbar navbar-default"
        :role "navigation"}
    (dom/div
     #js{:className "container-fluid"}
     (dom/div
      #js{:className "navbar-header"}
      (brand (:title data))
      (nav-collapse-button))
     (nav-items (:items data))
     ))))

(def nav-data
  {:title {:label "Webcasting" :href "#"}
   :items [{:label "Hosts"  :href "#" :active true}
           {:label "Events" :href "#"}]})

(defn app [data owner]
  (om/component
   (dom/div
    nil
    (om/build nav nav-data)
    (om/build entity/component comp-test/hosts-data))))

(defn render! []
  (om/root app {}
           {:target (js/document.getElementById "app")}))

(render!)
