(ns day8.re-frame.http-fx-2-example.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as re-frame :refer [subscribe dispatch]]
   [re-com.core :as re-com]
   ["react-highlight.js" :default react-highlightjs]
   ["highlight.js/lib/languages/clojure"]
   [day8.re-frame.http-fx-2-example.events :as events]
   [day8.re-frame.http-fx-2-example.routes :as routes]
   [day8.re-frame.http-fx-2-example.subs :as subs]))

(def highlight (r/adapt-react-class react-highlightjs))

(defn state-history
  []
  (let [history (subscribe [::subs/history])]
    [:table.table.table-striped.table-bordered
     {:style {:margin-top "50px"}}
     [:thead
      [:tr
       [:th "Step"]
       [:th "Request ID"]
       [:th "State Handler"]]]
     (for [row @history]
       ^{:key (gensym)}
       [:tbody
        [:tr
         [:td {:row-span 2} (:i row)]
         [:td [:pre (get-in row [:request-state :request-id])]]
         [:td [:pre (:state-handler row)]]]
        [:tr
         [:td {:col-span 2}
          [highlight {:language "clojure"}
           (:request-state row)]]]])]))

(defn server-knobs
  []
  (let [server (subscribe [::subs/server])]
    [re-com/v-box
     :children [[re-com/h-box
                 :children [[re-com/label
                             :label "Endpoint"]
                            [re-com/gap :size "12px"]
                            [re-com/single-dropdown
                             :model (:endpoint @server)
                             :on-change #(dispatch [::events/set [:server :endpoint] %])
                             :choices (:endpoints @server)]]]
                [re-com/gap :size "19px"]
                (when-not (= :success (:endpoint @server))
                  [re-com/h-box
                   :children [[re-com/label
                               :label "Frequency"]
                              [re-com/gap :size "12px"]
                              [re-com/slider
                               :model (:frequency @server)
                               :on-change #(dispatch [::events/set [:server :frequency] %])
                               :min 10
                               :max 100
                               :step 10
                               :width "343px"]
                              [re-com/gap :size "12px"]
                              [re-com/label
                               :label (str (:frequency @server)"%")]]])]]))

(defn code
  []
  (let [handler (subscribe [::subs/handler])]
    [re-com/box
     :width "555px"
     :child [highlight {:class-name "clojure"}
             @handler]]))

(defn buttons
  []
  (let [state (subscribe [::subs/state])]
    [re-com/h-box
     :justify :center
     :children [[re-com/button
                 :label "Go!"
                 :disabled? (not= :idle @state)
                 :on-click #(dispatch [::events/http-go])
                 :style {:width "131px"
                         :background-color "#2ECC40"
                         :border "1px solid #111"
                         :color "#111"}]
                [re-com/gap :size "31px"]
                [re-com/button
                 :label "Abort!"
                 :disabled? (= :idle @state)
                 :on-click #(dispatch [::events/http-abort])
                 :style {:width "131px"
                         :background-color "#FF4136"
                         :border "1px solid #111"
                         :color "#111"}]]]))

(defn knobs
  []
  (let []
    [re-com/v-box
     :style {:margin-top "50px"
             :padding-left "50px"
             :padding-right "50px"}
     :children [[server-knobs]
                [re-com/gap :size "31px"]
                [buttons]
                [re-com/gap :size "31px"]
                [code]]]))

(defn home-panel
  []
  [re-com/h-box
   :children [[knobs]
              [state-history]]])

(defn title
  []
  [re-com/h-box
   :class "re-frame-http-fx-2-example-title"
   :align :center
   :children [[re-com/md-icon-button
               :md-icon-name "zmdi-network-alert"
               :size :larger]
              [re-com/gap
               :size "48px"]
              [re-com/title
               :label "re-frame HTTP effect 2 example"
               :level :level1]]])

(defn header
  []
  [re-com/box
   :class "re-frame-http-fx-2-example-header"
   :child [re-com/h-box
           :justify :between
           :children [[title]]]])

(defn footer
  []
  [re-com/v-box
   :class "re-frame-http-fx-2-example-footer"
   :align :center
   :justify :center
   :children [[:div.re-frame-http-fx-2-example-copyright
               [:span {:dangerouslySetInnerHTML {:__html "&copy;"}}]
               "2019 Day8 Technology Pty Ltd, Australia."]
              [:div.re-frame-http-fx-2-example-license
               "The MIT License (MIT)"]]])

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [re-com/v-box
     :height "100vh"
     :justify :between
     :children [[header]
                [panels @active-panel]
                [footer]]]))

