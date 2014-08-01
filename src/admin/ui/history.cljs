(ns admin.ui.history
  (:require [goog.events :as events]
            [uri.core    :as uri])
  (:import goog.history.Html5History
           goog.history.EventType))

(def history (new goog.history.Html5History))

(defn defer [f]
  (js/setTimeout f 0))

(defn watch [f]
  (defer
    (fn []
      (events/listen history goog.history.EventType.NAVIGATE
                     (fn [e] (f (.-token e))))
      (.setEnabled history true))))

(defn goto! [loc]
  (.setToken history (uri/relative loc)))

(defn replace! [loc]
  (.replaceToken history (uri/relative loc)))

(defn location []
  (.getToken history))
