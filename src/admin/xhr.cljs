(ns admin.xhr
  (:require [cljs.reader :as reader]
            [goog.events :as events]
            [uri.core    :as uri])
  (:import goog.net.XhrIo
           goog.net.EventType))

(defn req [{:keys [method url type data on-complete]}]
  (let [xhr (new goog.net.XhrIo)]

    (when on-complete
      (events/listen xhr goog.net.EventType.COMPLETE
        (fn [e] (on-complete xhr e))))

    (.send xhr url method data headers)

    xhr))
