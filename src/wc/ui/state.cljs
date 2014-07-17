(ns wc.ui.state)

(def state (atom {}))

(defn on-entity! [ent]
  (swap! state assoc :entity ent)
  #_(debug/inspect! @state))

(defn perform-action! [action]
  (swap! state assoc :action action :form {}))

(defn cancel-action! []
  (swap! state dissoc :action :form))
