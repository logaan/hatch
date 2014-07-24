(ns wc.ui.app
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [uri.core      :as uri]
            [wc.ui.state   :as state]
            [wc.ui.entity  :as entity]
            [wc.ui.action  :as action]
            [wc.ui.history :as history]))

(defn component [data owner]
  (reify
    om/IRender
    (render [this]
      (cond
        (:action data) (om/build action/component (select-keys data [:action :form]))
        (:entity data) (om/build entity/component (:entity data))
        :else          (dom/div nil)))))

(defn init! []
  (history/init
   (fn [token]
     (state/present!
      (if (= token "") "/" token)))))
