(ns check-css.core
  (:require [check-css.browser :as b]
            [check-css.page :as p]
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

(defn -main [& args]
  (let [{:keys [options arguments summary help]} (parse-opts args cli-options)
        {:keys [browser selector path]} options
        urls arguments
        apply-script-fn (partial p/execute-script-fn path selector)]
    (if help
      (exit 0 summary)
      (b/exec-site-fn urls apply-script-fn))))
