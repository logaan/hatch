(ns admin.ui.state
  (:require [uri.core   :as uri]
            [siren.core :as siren]))

(defn req [href]
  {:cmd    :req
   :method "GET"
   :url    href})

(defn transition
  "take a state and an event and produce a pair of a new state and a list of commands"
  [st ev] ;; => [st+ [cmd...]]
  (condp = (:type ev)
    :present [st [(req (:href ev))]]))
