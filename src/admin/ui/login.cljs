(ns admin.ui.login
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true]))

(defn update-key [cursor k]
  (fn [e] (om/update! cursor k (.. e -target -value))))

(defn component
  [{:keys [username password on-login] :as cursor} owner]
  (om/component
   (dom/form
    #js{:className "form-signin"
        :role      "form"
        :onSubmit  (fn [e]
                     (.preventDefault e)
                     (when on-login
                       (on-login cursor)))}
    (dom/h2
     #js{:className "form-signin-heading"}
     "Please sign in")
    (dom/input
     #js{:type "text"
         :className "form-control"
         :placeholder "Username"
         :required true
         :autofocus true
         :value username
         :onChange (update-key cursor :username)})
    (dom/input
     #js{:type "password"
         :className "form-control"
         :placeholder "Password"
         :required true
         :value password
         :onChange (update-key cursor :password)})
    (dom/button
     #js{:type "submit"
         :className "btn btn-primary bnt-block"}
     "Sign in")
    )))
