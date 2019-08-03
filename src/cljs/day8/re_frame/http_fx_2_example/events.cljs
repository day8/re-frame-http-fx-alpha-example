(ns day8.re-frame.http-fx-2-example.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx]]
   [day8.re-frame.http-fx-2-example.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [day8.re-frame.http-fx-2]))

(reg-event-fx
  ::initialize
  (fn-traced [_ _]
    {:db   db/default-db
     :http {:action :reg-profile
            :id     :example
            :values {:mode          "cors"
                     :credentials   "omit"
                     :content-types {#"application/.*json.*" :json}
                     :fsm           {:in-setup     [::http-in-setup]
                                     :in-process   [::http-in-process]
                                     :in-problem   [::http-in-problem]
                                     :in-failed    [::http-in-failed]
                                     :in-succeeded [::http-in-succeeded]
                                     :in-done      [::http-in-done]}}}}))

(reg-event-fx
  ::http-in-setup
  (fn-traced [{:keys [db]} [_ {:keys [request-id context]}]]
    {:db   (assoc-in db (conj (:path context) :request-id) request-id)
     :http {:action     :trigger
            :trigger    :send
            :request-id request-id}}))

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
      {:db   (assoc db :state :in-requested)
       :http {:action   :GET
              :profiles [:example]
              :get      uri
              :params   {:frequency frequency}
              :timeout  timeout
              :context  {:path        [:http :example]
                         :max-retries max-retries}}})))

(reg-event-fx
  ::http-abort
  (fn-traced [{:keys [db]} _]
    (let [request-id (get-in db [:http :example :request-id])]
      {:http {:action     :abort
              :request-id request-id}})))

(reg-event-fx
  ::http-in-problem
  (fn-traced [{:keys [db]} [_ {:keys [request-id context problem] :as request-state}]]
    (let [{:keys [db-path max-retries]} context
          temporary? (= :timeout problem)
          num-retires (get-in db (conj db-path :num-retries) 0)
          try-again? (and temporary? (< num-retires max-retries))]
      (if try-again?
        {:db   (-> db
                   (update-in (conj db-path :history) conj {:state-handler :in-problem
                                                            :request-state request-state})
                   (update-in (conj db-path :num-retries) inc))
         :http {:action     :trigger
                :trigger    :retry
                :request-id request-id}}
        {:http {:action     :trigger
                :trigger    :fail
                :request-id request-id}}))))

(reg-event-fx
  ::http-in-failed
  (fn-traced [{:keys [db]} [_ {:keys [request-id]}]]
    {:http {:action     :trigger
            :trigger    :done
            :request-id request-id}}))

(reg-event-fx
  ::http-in-process
  (fn-traced [{:keys [db]} [_ {:keys [request-id context] :as request-state} res]]
    {:db   (assoc-in db (:path context) (:body res))
     :http {:action     :trigger
            :trigger    :processed
            :request-id request-id}}))

(reg-event-fx
  ::http-in-succeeded
  (fn-traced [{:keys [db]} [_ {:keys [request-id] :as request-state}]]
    {:http {:action     :trigger
            :trigger    :done
            :request-id request-id}}))

(reg-event-fx
  ::http-in-done
  (fn-traced [{:keys [db]} [_ req]]))