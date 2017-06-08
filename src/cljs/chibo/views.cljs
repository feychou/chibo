(ns chibo.views
  (:require [re-frame.core :as re-frame]))

(defn main-panel []
  (let [alphabet (re-frame/subscribe [:alphabet])]
    (fn []
      [:div "Hello from " @alphabet])))
