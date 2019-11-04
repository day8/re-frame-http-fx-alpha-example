(ns day8.re-frame.http-fx-alpha-example.handler
  (:require
    [clojure.java.io :as io]
    [compojure.core :refer [GET POST defroutes]]
    [compojure.route :refer [resources]]
    [ring.util.http-response :refer :all]
    [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
    [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]))

(defroutes routes
  (GET "/"
       [http-roots]
       (let [headers {"content-type" "text/html; charset=utf-8"}
             index-file
             (reduce
               (fn [_ http-root]
                 (let [file (io/file http-root "index.html")]
                   (when (and file (.exists file))
                     (reduced file))))
               nil
               http-roots)]
         {:status 200
          :headers headers
          :body (slurp index-file)}))

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
         (internal-server-error "ERROR")
         (ok "OK"))))

(def api-handler (-> #'routes
                     wrap-keyword-params
                     wrap-json-body
                     wrap-json-response
                     (wrap-defaults api-defaults)))
