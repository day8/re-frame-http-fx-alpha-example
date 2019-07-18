(ns day8.re-frame.http-fx-2-example.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame :refer [subscribe dispatch]]
   [re-com.core :as re-com]
   [day8.re-frame.http-fx-2-example.events :as events]
   [day8.re-frame.http-fx-2-example.routes :as routes]
   [day8.re-frame.http-fx-2-example.subs :as subs]))

(defn fsm
  []
  (let [state (subscribe [::subs/state])]
    [:svg {:xmlns "http://www.w3.org/2000/svg"
           :xmlnsXlink "http://www.w3.org/1999/xlink"
           :contentScriptType "application/ecmascript"
           :contentStyleType "text/css"
           :height "842px"
           :preserveAspectRatio "none"
           :style {:margin-top "50px"
                   :width "647px"
                   :height "842px"}
           :version "1.1"
           :viewBox "0 0 647 842"
           :width "647px"
           :zoomAndPan "magnify"}
     [:defs]
     [:g
      [:rect {:fill "#ADD8E6"
              :class (if (= :in-requested @state) "pulse" "")
              :height "40"
              :rx "12.5"
              :ry "12.5"
              :style {:stroke "#4169E1"
                      :stroke-width "1.5"}
              :width "87"
              :x "7.6973"
              :y "105"}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "14"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "77"
              :x "12.6973"
              :y "129.8467"} "::in-requested"]
      [:rect {:fill "#ADD8E6"
              :class (if (= :in-wait @state) "pulse" "")
              :height "64.2344"
              :rx "12.5"
              :ry "12.5"
              :style {:stroke "#4169E1"
                      :stroke-width "1.5"}
              :width "177"
              :x "118.697"
              :y "222"}]
      [:line {:style {:stroke "#4169E1"
                      :stroke-width "1.5"}
              :x1 "118.697"
              :x2 "295.697"
              :y1 "248.2969"
              :y2 "248.2969"}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "14"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "50"
              :x "182.197"
              :y "239.9951"} "::in-wait"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "12"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "157"
              :x "123.697"
              :y "264.4355"} "for a network connection"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "12"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "142"
              :x "123.697"
              :y "278.4043"} "and a server response"]
      [:rect {:class (if (= :in-process @state) "pulse" "")
              :fill "#ADD8E6"
              :height "78.2031"
              :rx "12.5"
              :ry "12.5"
              :style {:stroke "#4169E1"
                      :stroke-width "1.5"}
              :width "175"
              :x "284.697"
              :y "393"}]
      [:line {:style {:stroke "#4169E1"
                      :stroke-width "1.5"}
              :x1 "284.697"
              :x2 "459.697"
              :y1 "419.2969"
              :y2 "419.2969"}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "14"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "77"
              :x "333.697"
              :y "410.9951"} "::in-process"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "12"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "154"
              :x "289.697"
              :y "435.4355"} "take the response body,"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "12"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "155"
              :x "289.697"
              :y "449.4043"} "processes it and place it"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "12"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "59"
              :x "289.697"
              :y "463.373"} "in app-db"]
      [:rect {:class (if (= :in-succeeded @state) "pulse" "")
              :fill "#ADD8E6"
              :height "40"
              :rx "12.5"
              :ry "12.5"
              :style {:stroke "#4169E1"
                      :stroke-width "1.5"}
              :width "89"
              :x "336.697"
              :y "593"}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "14"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "79"
              :x "341.697"
              :y "617.8467"} "::in-succeeded"]
      [:rect {:class (if (= :in-done @state) "pulse" "")
              :fill "#ADD8E6"
              :height "40"
              :rx "12.5"
              :ry "12.5"
              :style {:stroke "#4169E1"
                      :stroke-width "1.5"}
              :width "50"
              :x "356.197"
              :y "710"}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "14"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "38"
              :x "362.197"
              :y "734.8467"} "::in-done"]
      [:rect {:class (if (= :in-problem @state) "pulse" "")
              :fill "#CCCCCC"
              :height "40"
              :rx "12.5"
              :ry "12.5"
              :style {:stroke "#AAAAAA"
                      :stroke-width "1.5"}
              :width "67"
              :x "75.6973"
              :y "412"}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "14"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "57"
              :x "80.6973"
              :y "436.8467"} "::in-problem"]
      [:ellipse {:cx "51.1973"
                 :cy "18"
                 :fill "#000000"
                 :rx "10"
                 :ry "10"
                 :style {:stroke "none"
                         :stroke-width "1.0"}}]
      [:rect {:class (if (= :in-failed @state) "pulse" "")
              :fill "#CCCCCC"
              :height "40"
              :rx "12.5"
              :ry "12.5"
              :style {:stroke "#AAAAAA"
                      :stroke-width "1.5"}
              :width "50"
              :x "133.197"
              :y "593"}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "14"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "40"
              :x "138.197"
              :y "617.8467"} "::in-failed"]
      [:rect {:class (if (= :in-cancelled @state) "pulse" "")
              :fill "#CCCCCC"
              :height "40"
              :rx "12.5"
              :ry "12.5"
              :style {:stroke "#AAAAAA"
                      :stroke-width "1.5"}
              :width "78"
              :x "477.197"
              :y "512"}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "14"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "68"
              :x "482.197"
              :y "536.8467"} "::in-cancelled"]
      [:ellipse {:cx "381.197"
                 :cy "821"
                 :fill "none"
                 :rx "10"
                 :ry "10"
                 :style {:stroke "#000000"
                         :stroke-width "1.0"}}]
      [:ellipse {:cx "381.697"
                 :cy "821.5"
                 :fill "#000000"
                 :rx "6"
                 :ry "6"
                 :style {:stroke "none"
                         :stroke-width "1.0"}}]
      [:path#*start-Requested {:d "M51.1973,28.318 C51.1973,44.116 51.1973,76.653 51.1973,99.376 "
                               :fill "none"
                               :style {:stroke "#4169E1"
                                       :stroke-width "1.0"}}]
      [:polygon {:fill "#4169E1"
                 :points "51.1973,104.638,55.1973,95.638,51.1973,99.638,47.1973,95.638,51.1973,104.638"
                 :style {:stroke "#4169E1"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "49"
              :x "52.1973"
              :y "71.0669"} "request"]
      [:path#Requested-Waiting {:d "M74.5521,145.013 C98.289,164.337 135.594,194.708 164.605,218.325 "
                                :fill "none"
                                :style {:stroke "#4169E1"
                                        :stroke-width "1.0"}}]
      [:polygon {:fill "#4169E1"
                 :points "168.822,221.759,164.3688,212.9744,164.9448,218.6018,159.3174,219.1778,168.822,221.759"
                 :style {:stroke "#4169E1"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "28"
              :x "130.197"
              :y "188.0669"} "sent"]
      [:path#Requested-Failed {:d "M43.2168,145.086 C27.3866,185.405 -5.1845,282.19 10.1973,363 C28.068,456.886 30.2431,487.777 89.1973,563 C99.7827,576.506 115.072,587.888 128.569,596.272 "
                               :fill "none"
                               :style {:stroke "#777777"
                                       :stroke-width "1.0"}}]
      [:polygon {:fill "#777777"
                 :points "133.068,599,127.4465,590.913,128.7927,596.4074,123.2983,597.7535,133.068,599"
                 :style {:stroke "#777777"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "56"
              :x "11.1973"
              :y "336.5669"} "incorrect"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "49"
              :x "14.6973"
              :y "351.6997"} "request"]
      [:path#Problem-Requested {:d "M102.9256,411.742 C98.5867,398.174 92.7903,379.549 88.1973,363 C78.7707,329.035 75.4029,320.698 69.1973,286 C60.684,238.399 55.4569,182.068 52.9623,150.543 "
                                :fill "none"
                                :style {:stroke "#777777"
                                        :stroke-width "1.0"}}]
      [:polygon {:fill "#777777"
                 :points "52.5566,145.327,49.2681,154.6106,52.9452,150.3119,57.2439,153.9889,52.5566,145.327"
                 :style {:stroke "#777777"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "30"
              :x "70.1973"
              :y "258.5669"} "retry"]
      [:path#Problem-Failed {:d "M103.9918,452.018 C97.8565,478.212 90.3594,526.314 106.197,563 C111.102,574.36 120.079,584.307 129.211,592.217 "
                             :fill "none"
                             :style {:stroke "#777777"
                                     :stroke-width "1.0"}}]
      [:polygon {:fill "#777777"
                 :points "133.122,595.48,128.7726,586.6435,129.2823,592.2774,123.6485,592.7871,133.122,595.48"
                 :style {:stroke "#777777"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "18"
              :x "157.697"
              :y "514.0669"} "fail"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "119"
              :x "107.197"
              :y "529.1997"} "because too many"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "107"
              :x "113.197"
              :y "544.3325"} "retries or error is"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "91"
              :x "121.197"
              :y "559.4653"} "unrecoverable"]
      [:path#Waiting-Problem {:d "M134.359,286.107 C123.075,294.172 112.942,304.047 106.197,316 C90.4366,343.931 95.6283,382.069 101.6884,406.62 "
                              :fill "none"
                              :style {:stroke "#777777"
                                      :stroke-width "1.0"}}]
      [:polygon {:fill "#777777"
                 :points "102.9875,411.644,104.6076,401.9293,101.7361,406.8031,96.8622,403.9316,102.9875,411.644"
                 :style {:stroke "#777777"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "50"
              :x "107.197"
              :y "344.0669"} "timeout"]
      [:path#Waiting-Problem-1 {:d "M196.683,286.301 C188.649,308.444 176.535,338.441 162.197,363 C152.977,378.794 140.484,395.003 129.943,407.632 "
                                :fill "none"
                                :style {:stroke "#777777"
                                        :stroke-width "1.0"}}]
      [:polygon {:fill "#777777"
                 :points "126.451,411.772,135.3112,407.4712,129.6746,407.9499,129.1959,402.3134,126.451,411.772"
                 :style {:stroke "#777777"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "82"
              :x "209.697"
              :y "329.0669"} "unsuccessful"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "129"
              :x "186.197"
              :y "344.1997"} "(network connection"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "97"
              :x "202.197"
              :y "359.3325"} "or server error)"]
      [:path#Waiting-Processing {:d "M285.562,286.107 C299.69,294.276 313.352,304.189 324.197,316 C342.708,336.159 354.645,364.476 361.986,387.966 "
                                 :fill "none"
                                 :style {:stroke "#4169E1"
                                         :stroke-width "1.0"}}]
      [:polygon {:fill "#4169E1"
                 :points "363.483,392.9,364.6953,383.126,362.0301,388.1157,357.0405,385.4506,363.483,392.9"
                 :style {:stroke "#4169E1"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "51"
              :x "383.197"
              :y "336.5669"} "success"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "109"
              :x "354.197"
              :y "351.6997"} "(a 200 response)"]
      [:path#Waiting-Cancelled {:d "M295.867,265.06 C363.039,274.516 447.797,290.998 472.197,316 C523.083,368.142 522.499,462.351 518.945,506.687 "
                                :fill "none"
                                :style {:stroke "#777777"
                                        :stroke-width "1.0"}}]
      [:polygon {:fill "#777777"
                 :points "518.5,511.84,523.2592,503.2173,518.93,506.8585,515.2888,502.5293,518.5,511.84"
                 :style {:stroke "#777777"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "34"
              :x "551.697"
              :y "329.0669"} "abort"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "133"
              :x "502.197"
              :y "344.1997"} "(the user terminated"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "79"
              :x "529.197"
              :y "359.3325"} "the request)"]
      [:path#Processing-Failed {:d "M329.638,471.349 C319.009,480.992 307.659,491.345 297.197,501 C267.622,528.295 262.677,537.924 231.197,563 C217.467,573.937 201.342,584.936 187.743,593.734 "
                                :fill "none"
                                :style {:stroke "#777777"
                                        :stroke-width "1.0"}}]
      [:polygon {:fill "#777777"
                 :points "183.236,596.63,192.9704,595.1331,187.4433,593.9285,188.648,588.4013,183.236,596.63"
                 :style {:stroke "#777777"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "69"
              :x "298.197"
              :y "529.0669"} "processing"]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "31"
              :x "317.197"
              :y "544.1997"} "error"]
      [:path#Processing-Succeeded {:d "M374.128,471.4 C375.898,506.602 378.465,557.664 379.97,587.588 "
                                   :fill "none"
                                   :style {:stroke "#4169E1"
                                           :stroke-width "1.0"}}]
      [:polygon {:fill "#4169E1"
                 :points "380.238,592.921,383.7804,583.7313,379.9866,587.9273,375.7905,584.1335,380.238,592.921"
                 :style {:stroke "#4169E1"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "66"
              :x "380.197"
              :y "536.5669"} "processed"]
      [:path#Succeeded-Done {:d "M381.197,633.248 C381.197,652.818 381.197,683.207 381.197,704.487 "
                             :fill "none"
                             :style {:stroke "#4169E1"
                                     :stroke-width "1.0"}}]
      [:polygon {:fill "#4169E1"
                 :points "381.197,709.689,385.197,700.689,381.197,704.689,377.197,700.689,381.197,709.689"
                 :style {:stroke "#4169E1"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "32"
              :x "382.197"
              :y "676.0669"} "done"]
      [:path#Failed-Done {:d "M183.345,626.968 C224.778,648.335 307.201,690.84 351.46,713.664 "
                          :fill "none"
                          :style {:stroke "#777777"
                                  :stroke-width "1.0"}}]
      [:polygon {:fill "#777777"
                 :points "356.142,716.079,349.9757,708.3994,351.6979,713.7877,346.3096,715.5099,356.142,716.079"
                 :style {:stroke "#777777"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "32"
              :x "284.197"
              :y "676.0669"} "done"]
      [:path#Cancelled-Done {:d "M505.292,552.139 C488.72,580.757 455.68,636.038 423.197,680 C416.674,688.828 408.993,698.076 401.995,706.128 "
                             :fill "none"
                             :style {:stroke "#777777"
                                     :stroke-width "1.0"}}]
      [:polygon {:fill "#777777"
                 :points "398.623,709.979,407.5625,705.8456,401.918,706.2182,401.5453,700.5737,398.623,709.979"
                 :style {:stroke "#777777"
                         :stroke-width "1.0"}}]
      [:text {:fill "#000000"
              :font-family "Roboto,sans-serif"
              :font-size "13"
              :lengthAdjust "spacingAndGlyphs"
              :textLength "32"
              :x "481.197"
              :y "617.5669"} "done"]
      [:path#Done-*end {:d "M381.197,750.3117 C381.197,766.8855 381.197,790.4101 381.197,805.4385 "
                        :fill "none"
                        :style {:stroke "#4169E1"
                                :stroke-width "1.0"}}]
      [:polygon {:fill "#4169E1"
                 :points "381.197,810.5623,385.197,801.5623,381.197,805.5623,377.197,801.5623,381.197,810.5623"
                 :style {:stroke "#4169E1"
                         :stroke-width "1.0"}}]]]))

(defn server-knobs
  []
  (let [server (subscribe [::subs/server])]
    [re-com/v-box
     :children [[re-com/title
                 :label "Server"
                 :level :level2
                 :style {:margin-top "31px"
                         :margin-bottom "31px"}]
                [re-com/h-box
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

(defn client-knobs
  []
  (let [client (subscribe [::subs/client])]
    [re-com/v-box
     :children [[re-com/title
                 :label "Client"
                 :level :level2
                 :style {:margin-top "31px"
                         :margin-bottom "31px"}]
                [re-com/h-box
                 :children [[re-com/label
                             :label "Timeout"]
                            [re-com/gap :size "12px"]
                            [re-com/slider
                             :model (:timeout @client)
                             :on-change #(dispatch [::events/set [:client :timeout] %])
                             :min 1
                             :max 10000
                             :step 10
                             :width "343px"]
                            [re-com/gap :size "12px"]
                            [re-com/label
                             :label (str (:timeout @client)"ms")]]]
                [re-com/gap :size "19px"]
                [re-com/h-box
                 :children [[re-com/label
                             :label "Max Retries"]
                            [re-com/gap :size "12px"]
                            [re-com/slider
                             :model (:max-retries @client)
                             :on-change #(dispatch [::events/set [:client :max-retries] %])
                             :min 0
                             :max 10
                             :step 1
                             :width "343px"]
                            [re-com/gap :size "12px"]
                            [re-com/label
                             :label (str (:max-retries @client)"x")]]]]]))

(defn knobs
  []
  [re-com/v-box
   :style {:margin-top "50px"
           :padding-left "50px"
           :padding-right "50px"}
   :children [[re-com/h-box
               :justify :center
               :children [[re-com/button
                           :label "Go!"
                           :on-click #(dispatch [::events/http-go])
                           :style {:width "131px"
                                   :background-color "#2ECC40"
                                   :border "1px solid #111"
                                   :color "#111"}]
                          [re-com/gap :size "31px"]
                          [re-com/button
                           :label "Abort!"
                           :on-click #(dispatch [::events/http-abort])
                           :style {:width "131px"
                                   :background-color "#FF4136"
                                   :border "1px solid #111"
                                   :color "#111"}]]]
              [server-knobs]
              [client-knobs]]])

(defn home-panel
  []
  [re-com/h-box
   :children [[knobs]
              [fsm]]])

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

