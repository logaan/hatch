(ns wc.ui.xhr
  (:require [goog.events :as events])
  (:import goog.net.XhrIo
           goog.net.EventType
           goog.Uri))

(defn format-query [url data]
  (let [uri (.parse goog.Uri url)]
    (doseq [[k v] data]
      (.setParameterValue uri (name k) (str v)))
    (.toString uri)))

(defn ^:private match [[method-pat type-pat] [method type]]
  (and (or (= method-pat :any) (= method-pat method))
       (or (= type-pat   :any) (= type-pat type))))

(defn format-data [method url type data]
  (let [edn "application/edn"
        post-headers #js{"Content-Type" type}]
   (condp match [method type]
     ["GET" :any] [(format-query url data) nil nil]
     [:any   edn] [url post-headers (pr-str data)]
     [:any   nil] [url nil nil])))

(defn req [{:keys [method url type data on-complete]}]
  (let [[url headers data] (format-data method url type data)
        xhr (new goog.net.XhrIo)]

    (when on-complete
      (events/listen xhr goog.net.EventType.COMPLETE
        (fn [e] (on-complete xhr e))))

    (.send xhr url method data headers)))
