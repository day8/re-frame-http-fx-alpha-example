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
  ::handler
  (fn [db _]
    (let [endpoint (get-in db [:server :endpoint])
          uri (case endpoint
                :invalid "http://i-do-not-exist/invalid"
                (str "http://localhost:8080/" (name endpoint)))
          code (goog.string/format "(reg-event-fx %s (fn-traced [_ _] {:http {:id :xyz :profiles [:example] :get %s}}))"
                    endpoint
                    uri)]
      code)))
      ;;(zprint/zprint-str code))))
