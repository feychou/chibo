(ns chibo.views
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn main-panel []
  (let [alphabets (subscribe [:alphabets])
        panel (subscribe [:panel])]
    (fn []
      (case @panel
          "alphabet-choice" [:div.container
                              [:button {:type "button"
                                        :value (first @alphabets)
                                        :on-click #(dispatch [:alphabet-picked (-> % .-target .-value)])}
                                        (first @alphabets)]
                              [:button {:type "button"
                                        :value (second @alphabets)
                                        :on-click #(dispatch [:alphabet-picked (-> % .-target .-value)])}
                                        (second @alphabets)]]
          "options" [:div.container
                      [:form
                        [:input {:id "free-text-true"
                                 :name "free-text"
                                 :type "radio"
                                 :value true
                                 :on-click #(dispatch [:quiz-options-updated {:free-text true}])}]
                        [:label {:for "free-text-true"} "Free text"]
                        [:input {:id "free-text-false"
                                 :name "free-text"
                                 :type "radio"
                                 :value false}]
                        [:label {:for "free-text-false"} "Multiple choice"]]
                      [:form
                        [:input {:id "from-romaji"
                                 :name "quiz-type"
                                 :type "radio"
                                 :value true}]
                        [:label {:for "from-romaji"} "From romaji"]
                        [:input {:id "to-romaji"
                                 :name "quiz-type"
                                 :type "radio"
                                 :value false}]
                        [:label {:for "to-romaji"} "To romaji"]
                        [:input {:id "mixed"
                                 :name "quiz-type"
                                 :type "radio"
                                 :value false}]
                        [:label {:for "mixed"} "Mixed"]]]))))