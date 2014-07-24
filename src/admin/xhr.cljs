(ns admin.xhr
  (:require [cljs.reader :as reader]
            [goog.events :as events]
            [uri.core    :as uri])
  (:import goog.net.XhrIo
           goog.net.EventType))

(defn ^:private match [[method-pat type-pat] [method type]]
  (and (or (= method-pat :any) (= method-pat method))
       (or (= type-pat   :any) (= type-pat type))))

(def edn "application/edn")

(defn format-data [method url type data]
  (condp match [method type]
    ["GET" :any] [(uri/add-query url data) nil nil]
    [:any   edn] [url #js{"Content-Type" type} (pr-str data)]
    [:any   nil] [url nil nil]))

(defn req [{:keys [method url type data on-complete]}]
  (let [[url headers data] (format-data method url type data)
        xhr (new goog.net.XhrIo)]

    (when on-complete
      (events/listen xhr goog.net.EventType.COMPLETE
        (fn [e] (on-complete xhr e))))

    (.send xhr url method data headers)

    xhr))

;; TODO make this a protocol or something

(defn ->edn    [xhr] (reader/read-string (.getResponseText xhr)))
(defn header   [xhr header] (.getResponseHeader xhr header))
(defn status   [xhr] (.getStatus xhr))
(defn abort    [xhr] (.abort xhr))
(defn last-uri [xhr] (.getLastUri xhr))

(def ok         200)
(def created    201)
(def no-content 204)
