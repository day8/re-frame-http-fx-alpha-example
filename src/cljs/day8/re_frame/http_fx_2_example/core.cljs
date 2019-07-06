(ns day8.re-frame.http-fx-2-example.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx-2-example.events :as events]
   [day8.re-frame.http-fx-2-example.routes :as routes]
   [day8.re-frame.http-fx-2-example.views :as views]
   [day8.re-frame.http-fx-2-example.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes re-frame/dispatch)
  (re-frame/dispatch-sync [::events/initialize])
  (dev-setup)
  (mount-root))
