(ns wc.ui.xhr
  (:require [cljs.reader :as reader]
            [goog.events :as events]
            [goog.dom :as gdom]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true])
  (:import [goog.net XhrIo]
           goog.net.EventType
           [goog.events EventType]))

(defn req [{:keys [method url data on-complete on-response]}]
  (let [xhr (XhrIo.)]

    (when on-complete
      (events/listen xhr goog.net.EventType.COMPLETE
        (fn [e]
          (on-complete (reader/read-string (.getResponseText xhr))))))

    (when on-response
      (events/listen xhr goog.net.EventType.COMPLETE
        (fn [e] (on-response xhr e))))

    (. xhr
      (send url method (when data (pr-str data))
        #js {"Content-Type" "application/edn"}))))
