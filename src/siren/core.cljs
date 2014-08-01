(ns siren.core)

(defn get-link [ent rel]
  (->> ent
       :links
       (filter #(some #{rel} (:rel %)))
       first
       :href))

(defn self [ent]
  (get-link ent "self"))

(defn get-action [ent action-name]
  (->> ent
       :actions
       (filter #(= action-name (:name %)))
       first))
