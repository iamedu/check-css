(ns check-css.page
  (:require [clj-webdriver.taxi :as taxi]
            [clojure.string :as s]
            [clojure.java.io :as io]))

(defn execute-script-fn [base-path js-src selector url]
  (let [path-url (s/replace url #"/" "_")
        path (str base-path "/" path-url ".png")]
    (do
      (taxi/execute-script (s/replace js-src #"#path" selector))
      (println (str "Checking site " url))
      (taxi/take-screenshot :file path)
      {:elements (->
                  (for [el (taxi/css-finder selector)]
                    {:xpath (taxi/xpath el)
                     :id (taxi/attribute el :id)
                     :value (taxi/value el)})
                  doall)
       :url url
       :screenshot-path path})))
