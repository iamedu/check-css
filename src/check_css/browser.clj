(ns check-css.browser
  (:require [clj-webdriver.taxi :as taxi]
            [clojure.string :as s]
            [clojure.java.io :as io]))

(defn exec-site-fn [urls f & {:keys [driver]
                              :or {driver {:browser :chrome}}}]
  (println driver)
  (taxi/with-driver driver
    (doseq [url urls]
      (taxi/to url)
      (f url))))
