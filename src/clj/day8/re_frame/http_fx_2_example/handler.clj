(ns day8.re-frame.http-fx-2-example.handler
  (:require
    [compojure.core :refer [GET POST defroutes]]
    [compojure.route :refer [resources]]
    [ring.util.http-response :refer :all]
    [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
    [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
    [ring.middleware.cors :refer [wrap-cors]]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]))

(defroutes routes
  (GET "/success"
       []
       (ok "OK"))
  (POST "/upload"
        []
        (Thread/sleep 3000)
        (ok "OK"))
  (GET "/timeout"
       [frequency]
       (when (<= (rand-int 101) (Integer/parseInt frequency))
         (Thread/sleep 10000))
       (ok "OK"))
  (GET "/failure"
       [frequency]
       (if (<= (rand-int 101) (Integer/parseInt frequency))
         (service-unavailable "ERROR")
         (ok "OK"))))

(def api-handler (-> #'routes
                     wrap-keyword-params
                     wrap-json-body
                     wrap-json-response
                     (wrap-defaults api-defaults)
                     (wrap-cors :access-control-allow-origin [#".*"]
                                :access-control-allow-methods [:get :post])))
