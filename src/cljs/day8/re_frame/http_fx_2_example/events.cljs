(ns day8.re-frame.http-fx-2-example.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx]]
   [day8.re-frame.http-fx-2-example.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [day8.re-frame.http-fx-2]))

(reg-event-fx
  ::initialize
  (fn-traced [_ _]
    {:db db/default-db
     :http {:reg-profile :example
            :values {:mode "cors"
                     :credentials "omit"
                     :content-types {#"application/.*json.*" :json}
                     :fsm {:in-wait [::http-in-wait]
                           :in-process [::http-in-process]
                           :in-problem [::http-in-problem]
                           :in-failed [::http-in-failed]
                           :in-succeeded [::http-in-succeeded]
                           :in-done [::http-in-done]}}}}))

(reg-event-db
  ::set-active-panel
  (fn-traced [db [_ active-panel]]
    (assoc db :active-panel active-panel)))

(reg-event-db
  ::set
  (fn-traced [db [_ path value]]
    (assoc-in db path value)))

(reg-event-fx
  ::http-go
  (fn-traced [{:keys [db]} _]
    (let [{{:keys [endpoint frequency]} :server} db
          {{:keys [timeout max-retries]} :client} db
          uri (if (= :invalid endpoint)
                "http://i-do-not-exist/invalid"
                (str "http://localhost:8080/" (name endpoint)))]
      {:db (assoc db :state :in-requested)
       :http {:id :xyz
              :profiles [:example]
              :get uri
              :params {:frequency frequency}
              :timeout timeout
              :context {:max-retries max-retries}}})))

(reg-event-fx
  ::http-abort
  (fn-traced [{:keys [db]} _]
    {:dispatch-later [{:ms 3000 :dispatch [::set [:state] :in-cancelled]}]
     :http {:abort :xyz}}))

(reg-event-fx
  ::http-in-wait
  (fn-traced [{:keys [db]} _]
    :dispatch-later [{:ms 3000 :dispatch [::set [:state] :in-wait]}]))


(reg-event-fx
  ::http-in-problem
  (fn-traced [{:keys [db]} [_ {:http/keys [context timeout] :as req} res {:http/keys [problem] :as err}]]
    (let [temporary? (= :problem/timeout problem)
          max-retries (:max-retries context)
          current-retires (get context :retry-count 0)
          try-again? (and (< current-retires max-retries) temporary?)]
      (if try-again?
        (let [re-req (-> req
                         (assoc-in [:http/context :retry-count] (inc current-retires)))]
          {:http re-req
           :dispatch-later [{:ms 3000 :dispatch [::set [:state] :in-problem]}]})
        {:dispatch-later [{:ms 3000 :dispatch [::set [:state] :in-problem]}]
         :http {:transition :failed
                :request req}}))))

(reg-event-fx
  ::http-in-failed
  (fn-traced [{:keys [db]} [_ req res]]
    {:dispatch-later [{:ms 3000 :dispatch [::set [:state] :in-failed]}]
     :http {:transition :done
            :request req}}))

(reg-event-fx
  ::http-in-process
  (fn-traced [{:keys [db]} [_ {:http/keys [context] :as req} res]]
    {:db (assoc-in db (:path context) (:body res))
     :dispatch-later [{:ms 3000 :dispatch [::set [:state] :in-process]}]
     :http {:transition :succeeded
            :request req}}))

(reg-event-fx
  ::http-in-succeeded
  (fn-traced [{:keys [db]} [_ req]]
    {:dispatch-later [{:ms 3000 :dispatch [::set [:state] :in-succeeded]}]
     :http {:transition :done
            :request req}}))

(reg-event-fx
  ::http-in-done
  (fn-traced [{:keys [db]} [_ req]]
    {:dispatch-later [{:ms 3000 :dispatch [::set [:state] :in-done]}
                      {:ms 6000 :dispatch [::set [:state] :idle]}]}))