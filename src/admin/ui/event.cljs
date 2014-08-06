(ns admin.ui.event)

(defn prevent-default [handler]
  (fn [ev]
    (.preventDefault ev)
    (handler ev)))
