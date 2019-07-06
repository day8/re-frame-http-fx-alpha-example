(ns day8.re-frame.http-fx-2-example.subs
  (:require
   [re-frame.core :refer [reg-sub]]))

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