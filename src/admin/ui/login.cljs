(ns admin.ui.login
  (:require [om.core :as om  :include-macros true]
            [om.dom  :as dom :include-macros true])
  (:import goog.storage.Storage
           goog.storage.mechanism.HTML5LocalStorage))

(def storage (goog.storage.Storage. (goog.storage.mechanism.HTML5LocalStorage.)))

(defn clear-password! [cursor]
  (om/update! cursor :password ""))

(defn logout! [cursor]
  (om/update! cursor :logged-in? false)
  (om/update! cursor :username "")
  (om/update! cursor :password "")
  (.remove storage "username")
  (.remove storage "password"))

(defn login! [cursor]
  (om/update! cursor :logged-in? true)
  (.set storage "username" (:username @cursor))
  (.set storage "password" (:password @cursor)))

(defn logged-in? [cursor]
  (:logged-in? cursor))

(defn login-from-localstorage [cursor]
  (let [username (.get storage "username")
        password (.get storage "password")]
    (when (and username password)
      (om/update! cursor :username username)
      (om/update! cursor :password password)
      (om/update! cursor :logged-in? true)
      true)))

(defn update-key [cursor k]
  (fn [e] (om/update! cursor k (.. e -target -value))))

(defn component [{:keys [data on-login]} owner]
  (reify
    om/IRender
    (render [this]
      (dom/form
       #js{:className "form-signin"
           :role      "form"
           :onSubmit  (fn [e]
                        (.preventDefault e)
                        (when on-login
                          (on-login data)))}
       (dom/h2
        #js{:className "form-signin-heading"}
        "Please sign in")
       (dom/input
        #js{:type "text"
            :className "form-control"
            :placeholder "Username"
            :required true
            :autofocus true
            :value (:username data)
            :onChange (update-key data :username)})
       (dom/input
        #js{:type "password"
            :className "form-control"
            :placeholder "Password"
            :required true
            :value (:password data)
            :onChange (update-key data :password)})
       (dom/button
        #js{:type "submit"
            :className "btn btn-primary bnt-block"}
        "Sign in")
       ))))
