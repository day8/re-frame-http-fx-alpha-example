(ns day8.re-frame.http-fx-alpha-example.routes
  (:import goog.History)
  (:require
   [accountant.core :as accountant]
   [secretary.core :as secretary :refer-macros [defroute]]
   [goog.events :as gevents]
   [goog.history.EventType :as EventType]
   [day8.re-frame.http-fx-alpha-example.events :as events]))

(def ^:dynamic *dispatch*
  (fn [event] (prn :dispatch event)))

(defroute home "/"  [query-params]
  (*dispatch* [::events/set-active-panel :home-panel query-params]))

(defn app-routes [dispatch]
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (binding [*dispatch* dispatch]
         (secretary/dispatch! path)))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})

  (accountant/dispatch-current!))

(defn navigate! [path]
  (accountant/navigate! path))
