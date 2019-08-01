(defproject day8.re-frame/http-fx-2-example "0.1.0-SNAPSHOT"
  :dependencies [[thheller/shadow-cljs "2.8.42"]
                 [ns-tracker "0.4.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [re-frame "0.10.8"]
                 [re-com "2.5.0"]
                 [clj-commons/secretary "1.2.4"]
                 [venantius/accountant "0.2.4"]
                 [garden "1.3.9"]
                 [day8.re-frame/http-fx-2 "2.0.0-SNAPSHOT"]
                 [compojure "1.6.1"
                  :exclusions [ring/ring-codec]]
                 [metosin/ring-http-response "0.9.1"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-devel "1.7.1"]
                 [ring-cors "0.1.13"]
                 [ring/ring-json "0.4.0"
                  :exclusions [ring/ring-core]]
                 [ring/ring-defaults "0.3.2"
                  :exclusions [ring/ring-core]]]

  :plugins [[lein-garden "0.3.0"]]

  :min-lein-version "2.9.1"

  :source-paths ["src/clj" "src/cljs"]

  :test-paths ["test/cljs"]

  :clean-targets ^{:protect false} [:target-path
                                    ".shadow-cljs"
                                    "resources/public/js/compiled"
                                    "resources/public/css"]

  :aliases {"dev" ["with-profile" "dev" "run" "-m" "shadow.cljs.devtools.cli" "watch" "app"]
            "prod" ["with-profile" "prod" "run" "-m" "shadow.cljs.devtools.cli" "release" "app"]}

  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   day8.re-frame.http-fx-2-example.css/screen
                     :compiler     {:output-to     "resources/public/css/screen.css"
                                    :pretty-print? true}}]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [day8.re-frame/re-frame-10x "0.4.2"]
                   [day8.re-frame/tracing "0.5.3"]]}

   :prod { :dependencies [[day8.re-frame/tracing-stubs "0.5.3"]]}})
