(ns chibo.views
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn alphabet-input [alphabet, input-name]
  (let [quiz-options @(subscribe [:quiz-options])]
    [:span
      [:input {:id (str input-name "-" alphabet)
               :name input-name
               :type "radio"
               :default-checked (= ((keyword input-name) quiz-options) alphabet)
               :on-click #(dispatch [:quiz-options-filtered {(keyword input-name) alphabet}])
               :value true}]
      [:label {:for (str input-name "-" alphabet)} alphabet]]))

(defn quiz-options []
  (let [quiz-options @(subscribe [:quiz-options])
        alphabets @(subscribe [:alphabets])]
    [:div.container
      [:form
        [:div
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
          [:label {:for "free-text-false"} "Multiple choice"]]]
        [:div "From"
          (for [alphabet alphabets]
            ^{:key alphabet} [alphabet-input alphabet "from"])]
        [:div "To"
          (for [alphabet alphabets]
            ^{:key alphabet} [alphabet-input alphabet "to"])]
        [:div
          [:button {:type "button"
                   :on-click #(dispatch [:quiz-started])}
                   "<<"]
          [:button {:type "button"
                   :on-click #(dispatch [:quiz-started])}
                   ">>"]]]))

(defn quiz []
  (let [current-char @(subscribe [:current-char])]
    [:div.container
      [:div.char (:h current-char)]
      [:input {:type "text"}]
      [:button {:type "button"
               :on-click #(dispatch [:next-char])}
               ">>"]]))

(defn main-panel [] 
  (let [panel (subscribe [:panel])]
    (fn []
      (case @panel
          "quiz-options" [quiz-options]
          "quiz" [quiz]))))