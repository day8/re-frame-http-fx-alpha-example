(ns day8.re-frame.http-fx-alpha-example.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx]]
   [day8.re-frame.http-fx-alpha-example.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [day8.re-frame.http-fx-alpha]))

(reg-event-fx
  ::initialize
  (fn-traced [_ _]
    {:db   db/default-db
     :http {:action :reg-profile
            :id     :example
            :values {:content-types {#"application/.*json.*" :json}
                     :fsm           {:in-setup     [::http-in-setup]
                                     :in-process   [::http-in-process]
                                     :in-problem   [::http-in-problem]
                                     :in-failed    [::http-in-failed]
                                     :in-succeeded [::http-in-succeeded]
                                     :in-teardown  [::http-in-teardown]}}}}))

(reg-event-fx
  ::http-in-setup
  (fn-traced [{:keys [db]} [_ {:keys [request-id context] :as request-state}]]
    (let [{:keys [db-path]} context]
      {:db   (-> db
                 (update-in (conj db-path :history) conj {:state-handler :in-setup
                                                          :request-state request-state})
                 (assoc-in (conj db-path :request-id) request-id))
       :http {:action     :trigger
              :trigger    :send
              :request-id request-id}})))

(reg-event-db
  ::set-active-panel
  (fn-traced [db [_ active-panel]]
    (assoc db :active-panel active-panel)))

(reg-event-db
  ::set
  (fn-traced [db [_ path value]]
    (assoc-in db path value)))

(reg-event-db
  ::set-files
  (fn-traced [db [_ files]]
    (assoc db :files files)))

(reg-event-fx
  ::http-go
  (fn-traced [{:keys [db]} _]
    (let [{{:keys [endpoint]} :server} db
          handler (case endpoint
                    :upload [::http-upload-files]
                    [::http-GET])]
      {:dispatch handler})))

(reg-event-fx
  ::http-GET
  (fn-traced [{:keys [db]} _]
    (let [{{:keys [endpoint frequency]} :server} db
          {{:keys [timeout max-retries]} :client} db
          url (if (= :invalid endpoint)
                "http://i-do-not-exist/invalid"
                (str "/" (name endpoint)))
          db-path [:http :example]]
      {:db   (assoc-in db (conj db-path :history) [])
       :http {:action   :GET
              :profiles [:example]
              :url      url
              :params   {:frequency frequency}
              :timeout  timeout
              :context  {:db-path     db-path
                         :max-retries max-retries}}})))

(reg-event-fx
  ::http-upload-files
  (fn-traced [{:keys [db]} _]
    (let [{{:keys [timeout max-retries]} :client} db
          url "/upload"
          db-path [:http :example]
          body (reduce
                 (fn [form-data file]
                   (.append form-data (.-name file) file)
                   form-data)
                 (js/FormData.)
                 (:files db))]
      {:db   (assoc-in db (conj db-path :history) [])
       :http {:action   :POST
              :profiles [:example]
              :url      url
              :body     body
              :timeout  timeout
              :context  {:db-path     db-path
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
  (fn-traced [{:keys [db]} [_ {:keys [request-id context] :as request-state}]]
    (let [{:keys [db-path]} context]
      {:db   (update-in db (conj db-path :history) conj {:state-handler :in-failed
                                                         :request-state request-state})
       :http {:action     :trigger
              :trigger    :done
              :request-id request-id}})))

(reg-event-fx
  ::http-in-process
  (fn-traced [{:keys [db]} [_ {:keys [request-id context response] :as request-state}]]
    (let [{:keys [db-path]} context]
      {:db   (-> db
                 (update-in (conj db-path :history) conj {:state-handler :in-process
                                                          :request-state request-state})
                 (assoc-in (conj db-path :body) (:body response)))
       :http {:action     :trigger
              :trigger    :processed
              :request-id request-id}})))

(reg-event-fx
  ::http-in-succeeded
  (fn-traced [{:keys [db]} [_ {:keys [request-id context] :as request-state}]]
    (let [{:keys [db-path]} context]
      {:db   (update-in db (conj db-path :history) conj {:state-handler :in-succeeded
                                                         :request-state request-state})
       :http {:action     :trigger
              :trigger    :done
              :request-id request-id}})))

(reg-event-fx
  ::http-in-teardown
  (fn-traced [{:keys [db]} [_ {:keys [request-id context] :as request-state}]]
    (let [{:keys [db-path]} context]
      {:db (update-in db (conj db-path :history) conj {:state-handler :in-teardown
                                                       :request-state request-state})})))