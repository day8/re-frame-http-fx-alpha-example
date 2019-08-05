(ns day8.re-frame.http-fx-2-example.css
  (:require
    [garden.def :refer [defstyles defkeyframes]]))

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
     :font-size "31px"}]])