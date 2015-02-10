(defproject au.com.silverpond/hatch "0.1.0-SNAPSHOT"
  :description "HTTP access tool for changes by humans"
  :url "https://github.com/silverpond/hatch/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2268"]
                 [om "0.6.5"]]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :cljsbuild
  {:builds {:prod {:source-paths ["src"]
                   :compiler {:output-to "resources/prod/js/main.js"
                              :output-dir "resources/prod/js"
                              :optimizations :advanced
                              :pretty-print false}}
            :test {:source-paths ["src" "test" "dev"]
                   :compiler {:output-to "resources/test/js/main.js"
                              :output-dir "resources/test/js"
                              :optimizations :none
                              :pretty-print true
                              :source-map "resources/test/js/main.js.map"}}}}
  :profiles {:dev {:dependencies [[ankha "0.1.4-SNAPSHOT"]]
                   :plugins [[com.cemerick/austin "0.1.4"]]}})
