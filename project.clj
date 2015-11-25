(defproject check-css "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.cli "0.3.3"]
                 [org.clojure/core.async "0.2.374"]
                 [clj-webdriver "0.7.2"]
                 [org.seleniumhq.selenium/selenium-java "2.47.1"]
                 [com.codeborne/phantomjsdriver "1.2.1"
                                                :exclusion [org.seleniumhq.selenium/selenium-java
                                                            org.seleniumhq.selenium/selenium-server
                                                            org.seleniumhq.selenium/selenium-remote-driver]]
                 [org.clojure/core.async "0.2.374"]]
  :main ^:skip-aot check-css.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
