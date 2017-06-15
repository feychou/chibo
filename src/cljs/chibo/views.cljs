(ns chibo.views
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn alphabet-choice []
  (let [alphabets @(subscribe [:alphabets])
        quiz-alphabet @(subscribe [:quiz-alphabet])]
    [:div.container
        [:input {:id (first alphabets)
                 :name "alphabet"
                 :type "radio"
                 :default-checked (= quiz-alphabet (first alphabets))
                 :value (first alphabets)
                 :on-click #(dispatch [:alphabet-picked (-> % .-target .-value)])}]
        [:label {:for (first alphabets)} (first alphabets)]
        [:input {:id (second alphabets)
                 :name "alphabet"
                 :type "radio"
                 :default-checked (= quiz-alphabet (second alphabets))
                 :value (second alphabets)
                 :on-click #(dispatch [:alphabet-picked (-> % .-target .-value)])}]
        [:label {:for (second alphabets)} (second alphabets)]
        [:button {:type "button"
                 :on-click #(dispatch [:panel-changed "quiz-options"])}
                 ">>"]]))

(defn quiz-options []
  (let [quiz-options @(subscribe [:quiz-options])]
    [:div.container
      [:form
        [:input {:id "free-text-true"
                 :name "free-text"
                 :type "radio"
                 :default-checked (:free-text quiz-options)
                 :value true
                 :on-click #(dispatch [:quiz-options-filtered {:free-text true}])}]
        [:label {:for "free-text-true"} "Free text"]
        [:input {:id "free-text-false"
                 :name "free-text"
                 :type "radio"
                 :default-checked (not (:free-text quiz-options))
                 :value false
                 :on-click #(dispatch [:quiz-options-filtered {:free-text false}])}]
        [:label {:for "free-text-false"} "Multiple choice"]]
      [:form
        [:input {:id "to-romaji"
                 :name "quiz-type"
                 :default-checked (= (:quiz-type quiz-options) "to-romaji")
                 :type "radio"
                 :value false
                 :on-click #(dispatch [:quiz-options-filtered {:quiz-type "to-romaji"}])}]
        [:label {:for "to-romaji"} "To romaji"]
        [:input {:id "from-romaji"
                 :name "quiz-type"
                 :default-checked (= (:quiz-type quiz-options) "from-romaji")
                 :type "radio"
                 :value true
                 :on-click #(dispatch [:quiz-options-filtered {:quiz-type "from-romaji"}])}]
        [:label {:for "from-romaji"} "From romaji"]
        [:input {:id "mixed"
                 :name "quiz-type"
                 :type "radio"
                 :default-checked (= (:quiz-type quiz-options) "mixed")
                 :value false
                 :on-click #(dispatch [:quiz-options-filtered {:quiz-type "mixed"}])}]
        [:label {:for "mixed"} "Mixed"]]
      [:button {:type "button"
               :on-click #(dispatch [:panel-changed "alphabet-choice"])}
               "<<"]
      [:button {:type "button"
               :on-click #(dispatch [:panel-changed "quiz"])}
               ">>"]]))

(defn main-panel []
  (let [panel (subscribe [:panel])]
    (fn []
      (case @panel
          "alphabet-choice" [alphabet-choice]
          "quiz-options" [quiz-options]))))