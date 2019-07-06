(ns day8.re-frame.http-fx-2-example.css
  (:require
    [garden.def :refer [defstyles defkeyframes]]))

(defkeyframes pulse
  [:0%
   {:stroke-width "1"
    :stroke-opacity "1"}]
  [:70%
   {:stroke-width "12"
    :stroke-opacity "0.4"}]
  [:100%
   {:stroke-width "1"
    :stroke-opacity "1"}])

(defstyles screen
  [:body
   {:font-family "'Roboto Condensed', sans-serif"}]

  [:.re-frame-http-fx-2-example-header
   :.re-frame-http-fx-2-example-footer
   {:height "81px"
    :background-color "#111"
    :color "#DDD"}]

  [:.re-frame-http-fx-2-example-title
   {:padding-left "48px"}
   [:.rc-md-icon-button
    {:height "31px"}]
   [:.rc-md-icon-button
    :.rc-title
    {:color "#DDD"
     :font-size "31px"}]]

  pulse

  [:svg
   [:.pulse
    {:animation [[pulse "0.5s" :infinite]]}]])