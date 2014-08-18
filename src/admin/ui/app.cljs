(ns admin.ui.app
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]
            [admin.ui.history :as history]
            [admin.ui.entity  :as entity]
            [admin.ui.action  :as action]
            [admin.ui.login   :as login]
            [admin.ui.state   :as state]))

(defn component [data owner]
  (reify
    om/IDidMount
    (did-mount [this]
      (state/init! data)
      (history/watch
       (fn [token]
         (state/present! data (if (= token "") "/" token)))))

    om/IRender
    (render [this]
      (cond
       (not (login/logged-in? (:auth data)))
         (om/build login/component {:data (:auth data)
                                    :on-login (state/make-login-handler data)})
       (:form   data) (om/build action/component (:form   data))
       (:entity data) (om/build entity/component (:entity data))
       :else          (dom/div nil)))))
