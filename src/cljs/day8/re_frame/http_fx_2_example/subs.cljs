(ns day8.re-frame.http-fx-2-example.subs
  (:require
   [re-frame.core :refer [reg-sub]]
   [zprint.core :as zprint]
   [goog.string]))

(reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(reg-sub
  ::server
  (fn [{:keys [server]} _]
    server))

(reg-sub
  ::client
  (fn [{:keys [client]} _]
    client))

(reg-sub
  ::state
  (fn [{:keys [state]} _]
    state))

(reg-sub
  ::history
  (fn [db _]
    (let [history (get-in db [:http :example :history])]
      (map-indexed
        (fn [i m]
          (-> m
            (assoc :i i)
            (update :request-state dissoc :fsm)))
        history))))
