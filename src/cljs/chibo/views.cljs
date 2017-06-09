(ns chibo.views
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn main-panel []
  (let [alphabets (subscribe [:alphabets])
        panel (subscribe [:panel])]
    (fn []
      (case @panel
          "alphabet-choice" [:div.container
                              [:button {:type "button" :value (first @alphabets) :on-click #(dispatch [:alphabet-picked (-> % .-value)])} (first @alphabets)]
                              [:button {:type "button" :value (second @alphabets) :on-click #(dispatch [:alphabet-picked (-> % .-value)])} (second @alphabets)]]
          "quiz" [:div.container
                    [:span.char]
                    [:ul.guesses
                      [:li]]]))))
