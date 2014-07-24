(ns admin.ui.state-test
  (:require [admin.ui.state :refer [transition]]))

(defn run []
  (assert
   (= (transition {} {:type :present :href "/foo"})
      [{} [{:cmd :req :method "GET" :url "/foo"}]])))
