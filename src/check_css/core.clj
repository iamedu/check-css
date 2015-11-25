(ns check-css.core
  (:require [check-css.browser :as b]
            [check-css.page :as p]
            [clojure.core.async :refer [chan go-loop <! <!! >! >!! close!]]
            [clojure.java.io :as io]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  [["-s" "--selector SELECTOR" "CSS Selector"]
   ["-p" "--path PATH" "The base folder for images"]
   ["-b" "--browser BROWSER" "Browser"
    :default :chrome
    :parse-fn keyword]
   ["-h" "--help"]])

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn publish-result [fun ch url]
  (>!! ch (fun url)))

(defn print-page-stats [element-data]
  (let [{:keys [elements url screenshot-path]} element-data]
    (println)
    (println (str "Data for url: " url))
    (println (str "Including " (count elements) " elements found"))
    (println (str "You can see the screenshot at: " screenshot-path))
    (doseq [{:keys [xpath id value]} elements]
      (println (str "Found element " xpath)))
    (println)))

(defn find-css-usages [browser selector output-path urls]
  (let [ch (chan)
        out-ch (chan)
        js-src (-> (io/resource "script.js") slurp)
        apply-script-fn (partial p/execute-script-fn
                                 output-path
                                 js-src
                                 selector)
        apply-script-fn (partial publish-result
                                 apply-script-fn
                                 ch)
        total-count (atom 0)]
    (go-loop []
      (if-let [{:keys [elements]
                  :as x} (<! ch)]
        (do
          (print-page-stats x)
          (swap! total-count + (count elements))
          (recur))
        (>! out-ch @total-count)))
    (b/exec-site-fn urls apply-script-fn)
    (close! ch)
    (println "There are " (<!! out-ch) " total elements affected by that class!")))

(defn -main [& args]
  (let [{:keys [options arguments summary]} (parse-opts args cli-options)
        {:keys [browser selector path help]} options
        urls arguments]
    (if-not help
      (find-css-usages browser selector path urls)
      (exit 0 summary))))
