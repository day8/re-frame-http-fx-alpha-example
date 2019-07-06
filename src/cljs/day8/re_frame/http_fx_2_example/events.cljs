(ns day8.re-frame.http-fx-2-example.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx]]
   [day8.re-frame.http-fx-2-example.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [day8.re-frame.http-fx-2]))

(reg-event-fx
  ::initialize
  (fn-traced [_ _]
    {:db db/default-db}))

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
          {{:keys [timeout max-retries]} :client} db]
      {:db (assoc db :state :in-requested)
       :http #:http {:with-profiles [endpoint]
                     :timeout timeout
                     :mode "cors"
                     :credentials "omit"
                     :content-types {#"application/.*json.*" :json}
                     :context {:max-retries max-retries}
                     :in-wait [::http-in-wait]
                     :in-process [::http-in-process]
                     :in-problem [::http-in-problem]
                     :in-failed [::http-in-failed]
                     :in-succeeded [::http-in-succeeded]
                     :in-done [::http-in-done]}})))

(reg-event-fx
  ::http-abort
  (fn-traced [{:keys [db]} _]))

(reg-event-db
  ::http-in-wait
  (fn-traced [db _]
    (assoc db :state :in-wait)))

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
           :db (assoc db :state :in-problem)})
        {:db (assoc db :state :in-problem)
         :http #:http {:transition :failed
                       :request req}}))))

(reg-event-fx
  ::http-in-failed
  (fn-traced [{:keys [db]} [_ req res]]
    {:db (assoc db :state :in-failed)
     :http #:http {:transition :done
                   :request req}}))

(reg-event-fx
  ::http-in-process
  (fn-traced [{:keys [db]} [_ {:http/keys [context] :as req} res]]
    {:db (-> db
             (assoc db :state :in-process)
             (assoc-in (:path context) (:body res)))
     :http #:http {:transition :succeeded
                   :request req}}))

(reg-event-fx
  ::http-in-succeeded
  (fn-traced [{:keys [db]} [_ req]]
    {:db (assoc db :state :in-succeeded)
     :http #:http {:transition :done
                   :request req}}))

(reg-event-db
  ::http-in-done
  (fn-traced [{:keys [db]} [_ req]]
    (assoc db :state :in-done)))