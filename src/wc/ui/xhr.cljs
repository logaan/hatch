(ns wc.ui.xhr
  (:require [cljs.reader :as reader]
            [goog.events :as events]
            [goog.dom :as gdom]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true])
  (:import goog.net.XhrIo
           goog.net.EventType
           goog.Uri))

(defn format-query [url data]
  (let [uri (.parse goog.Uri url)]
    (doseq [[k v] data]
      (.setParameterValue uri (name k) (str v)))
    (.toString uri)))

(defn format-url-enc [data]
  (let [uri (goog.Uri.)]
    (doseq [[k v] data]
      (.setParameterValue uri (name k) (str v)))
    (.getEncodedQuery uri)))

(defn format-json [data]
  (js/JSON.stringify (clj->js data)))

(defn ^:private match [[method-pat type-pat] [method type]]
  (and (or (= method-pat :any) (method-pat method))
       (or (= type-pat   :any) (= type-pat type))))

(def edn     "application/edn")
(def json    "application/json")
(def url-enc "application/x-www-form-urlencoded")

(defn format-data [method url type data]
  (let [post-headers #js{"Content-Type" type}]
   (condp match [method type]
     [#{"GET"}          :any]    [(format-query url data) nil nil]
     [#{"POST" "PATCH"} edn]     [url post-headers (pr-str data)]
     [#{"POST" "PATCH"} json]    [url post-headers (format-json data)]
     [#{"POST" "PATCH"} url-enc] [url post-headers (format-url-enc data)]
     [#{"POST" "PATCH"} :any]    (assert false)
     [:any              nil]     [url nil nil])))

(defn req [{:keys [method url type data on-complete]}]
  (let [[url headers data] (format-data method url type data)
        xhr (goog.net.XhrIo.)]

    (when on-complete
      (events/listen xhr goog.net.EventType.COMPLETE
        (fn [e] (on-complete xhr e))))

    (.send xhr url method data headers)))
