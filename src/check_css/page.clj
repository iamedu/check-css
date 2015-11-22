(ns check-css.page
  (:require [clj-webdriver.taxi :as taxi]
            [clojure.string :as s]
            [clojure.java.io :as io]))


(defn execute-script-fn [base-path selector url]
  (let [js-src (-> (io/resource "script.js")
                   (slurp))
        path-url (s/replace url #"/" "_")
        path (str base-path "/" path-url ".png")]
    (taxi/execute-script (s/replace js-src #"#path" selector))
    (println (str "Checking site " url))
    (taxi/take-screenshot :file path)))
