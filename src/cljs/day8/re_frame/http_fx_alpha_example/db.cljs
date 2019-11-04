(ns day8.re-frame.http-fx-alpha-example.db)

(def default-db
  {:state :idle

   :server {:endpoint :success
            :endpoints [{:id :success :label "Success"}
                        {:id :upload  :label "File Upload"}
                        {:id :failure :label "Failure"}
                        {:id :timeout :label "Timeout"}
                        {:id :invalid :label "Invalid"}]
            :frequency 80}

   :client {:timeout 5000
            :max-retries 3}})