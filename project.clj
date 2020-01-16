(defproject day8.re-frame/http-fx-alpha-example "lein-git-inject/version"

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs "2.8.83"]
                 [re-frame "0.10.9"]
                 [re-com "2.6.0"]
                 [clj-commons/secretary "1.2.4"]
                 [venantius/accountant "0.2.5"]
                 [garden "1.3.9"]
                 [ns-tracker "0.4.0"]
                 [day8.re-frame/http-fx-alpha "0.0.2"]
                 [compojure "1.6.1"
                  :exclusions [ring/ring-codec]]
                 [metosin/ring-http-response "0.9.1"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-devel "1.7.1"]
                 [ring-cors "0.1.13"]
                 [ring/ring-json "0.5.0"
                  :exclusions [ring/ring-core]]
                 [ring/ring-defaults "0.3.2"
                  :exclusions [ring/ring-core]]]

  :plugins      [[day8/lein-git-inject "0.0.11"]
                 [lein-shadow          "0.1.7"]
                 [lein-garden          "0.3.0"]]

  :middleware   [leiningen.git-inject/middleware]

  :min-lein-version "2.9.1"

  :source-paths ["src/clj" "src/cljs"]

  :test-paths ["test/cljs"]

  :clean-targets ^{:protect false} [:target-path
                                    "shadow-cljs.edn"
                                    "package.json"
                                    "package-lock.json"
                                    "resources/public/js/compiled"
                                    "resources/public/css"]

  :shadow-cljs {:nrepl  {:port 8777}

                :builds {:app
                         {:target     :browser
                          :output-dir "resources/public/js/compiled/out"
                          :asset-path "/js/compiled/out"
                          :dev        {:modules    {:base {:entries [devtools.preload
                                                                     day8.re-frame-10x.preload
                                                                     day8.re-frame.http-fx-alpha-example.core]}}
                                       :compiler-options {:closure-defines {re-frame.trace.trace-enabled?        true
                                                                            day8.re-frame.tracing.trace-enabled? true}}}
                          :release    {:modules {:base {:entries [day8.re-frame.http-fx-alpha-example.core]}}
                                       :compiler-options {:optimizations :advanced
                                                          :pretty-print false
                                                          :closure-defines {goog.DEBUG                           false
                                                                            re-frame.trace.trace-enabled?        false
                                                                            day8.re-frame.tracing.trace-enabled? false}}}
                          :devtools   {:http-root    "resources/public"
                                       :http-handler day8.re-frame.http-fx-alpha-example.handler/api-handler
                                       :http-port    8280
                                       :after-load   day8.re-frame.http-fx-alpha-example.core/mount-root
                                       :preloads     [devtools.preload
                                                      day8.re-frame-10x.preload]}}

                         :browser-test
                         {:target           :browser-test
                          :ns-regexp        "-test$"
                          :test-dir         "resources/public/js/compiled/browser-test/out"
                          :compiler-options {:closure-defines {re-frame.trace.trace-enabled?        true
                                                               day8.re-frame.tracing.trace-enabled? true}}

                          :devtools         {:http-root "resources/public/js/compiled/browser-test/out"
                                             :http-port 8290}}

                         :karma-test
                         {:target    :karma
                          :ns-regexp "-test$"
                          :output-to "resources/public/js/compiled/karma-test/out"}}}

  :aliases {"dev-auto" ["with-profile" "dev" "shadow" "watch" "app"]
            "prod" ["with-profile" "prod" "shadow" "release" "app"]}

  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   day8.re-frame.http-fx-alpha-example.css/screen
                     :compiler     {:output-to     "resources/public/css/screen.css"
                                    :pretty-print? true}}]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.11"]
                   [day8.re-frame/re-frame-10x "0.4.5"]
                   [day8.re-frame/tracing "0.5.3"]]}

   :prod { :dependencies [[day8.re-frame/tracing-stubs "0.5.3"]]}})
