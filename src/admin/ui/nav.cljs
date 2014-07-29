(ns admin.ui.nav
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]))

(defn nav-items [items]
  (dom/div
   #js{:className "navbar-collapse collapse"}
   (apply dom/ul
          #js{:className "nav navbar-nav"}
          (for [{:keys [label href active]} items]
            (dom/li
             #js{:className (if active "active")}
             (dom/a #js{:href href} label))))))

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

(defn brand [{:keys [label href]}]
  (dom/a #js{:className "navbar-brand" :href href} label))

(defn component [data owner]
  (om/component
   (dom/div
    #js{:className "navbar navbar-default"
        :role "navigation"}
    (dom/div
     #js{:className "container-fluid"}
     (dom/div
      #js{:className "navbar-header"}
      (brand (:title data))
      (collapse-button))
     (nav-items (:items data))
     ))))
