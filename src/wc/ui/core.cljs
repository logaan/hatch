(ns wc.ui.core
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]))

(def app-state (atom {}))

(swap! app-state assoc :list ["Lion" "Zebra" "Buffalo" "Antelope"])

(defn widget [data owner]
  (reify
    om/IRender
    (render [this]
      (apply dom/ul nil
        (map (fn [text] (dom/li nil (str "Item: " text)))
             (:list data))))))

(defn render! []
  (om/root widget app-state
   {:target (. js/document (getElementById "my-app"))}))

(render!)
