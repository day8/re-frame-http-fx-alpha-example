(ns day8.re-frame.http-fx-2-example.db)

(def default-db
  {:server {:endpoint :success
            :endpoints [{:id :success :label "Success"}
                        {:id :failure :label "Failure"}
                        {:id :timeout :label "Timeout"}
                        {:id :invalid :label "Invalid"}]
            :frequency 80}

   :client {:timeout 5000
            :max-retries 3}})