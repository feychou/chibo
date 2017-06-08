(ns chibo.views
  (:require [re-frame.core :as re-frame]))

(defn main-panel []
  (let [alphabets (re-frame/subscribe [:alphabets])
        panel (re-frame/subscribe [:panel])]
    (fn []
      (case @panel
          "alphabet-choice" [:div.container
                              [:button {:type "button" :value (first @alphabets)} (first @alphabets)]
                              [:button {:type "button" :value (second @alphabets)} (second @alphabets)]]))))
