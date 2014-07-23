(ns wc.ui.history
  (:require [goog.events :as events])
  (:import goog.History
           goog.history.EventType))

(def history (new goog.History))

(defn init [f]
  (events/listen history goog.history.EventType.NAVIGATE
                 (fn [e] (f (.-token e))))
  (.setEnabled history true))