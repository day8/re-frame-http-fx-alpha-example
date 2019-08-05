(ns day8.re-frame.http-fx-2-example.css
  (:require
    [garden.color :refer [rgba]]
    [garden.def :refer [defstyles defkeyframes]]
    [garden.selectors :refer [>]])
  (:refer-clojure :exclude [>]))

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

  [:.file-drop
   {:position "relative"
    :height "100px"
    :width "100%"}]

  [(> :.file-drop :.file-drop-target)
   {:position "absolute"
    :background-color "#39CCCC"
    :top 0
    :left 0
    :height "100%"
    :width "100%"
    :border-radius "2px"
    :display "flex"
    :flex-direction "column"
    :align-items "center"
    :justify-content "center"
    :align-content "center"
    :text-align "center"}]

  [(> :.file-drop :.file-drop-target.file-drop-dragging-over-frame)
   {:border "none"
    :background-color (rgba 0 0 0 0.65)
    :box-shadow "none"
    :z-index 50
    :opacity 1.0
    :color "white"}]

  [(> :.file-drop :.file-drop-target.file-drop-dragging-over-target)
   {:color "#ff6e40"
    :box-shadow "0 0 13px 3px #ff6e40"}])