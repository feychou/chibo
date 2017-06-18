(ns chibo.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :refer [atom]]))

(defn alphabet-input [alphabet, input-name]
  (let [quiz-options (subscribe [:quiz-options])]
    (fn []
      [:span
        [:input {:id (str input-name "-" alphabet)
                 :name input-name
                 :type "radio"
                 :default-checked (= ((keyword input-name) @quiz-options) alphabet)
                 :on-click #(dispatch [:quiz-options-filtered {(keyword input-name) alphabet}])
                 :value true}]
        [:label {:for (str input-name "-" alphabet)} alphabet]])))

(defn quiz-options []
  (let [quiz-options (subscribe [:quiz-options])
        alphabets (subscribe [:alphabets])]
    (fn []
      [:div.container
        [:form
          [:div
            [:input {:id "free-text-true"
                     :name "free-text"
                     :type "radio"
                     :default-checked (:free-text @quiz-options)
                     :value true
                     :on-click #(dispatch [:quiz-options-filtered {:free-text true}])}]
            [:label {:for "free-text-true"} "Free text"]
            [:input {:id "free-text-false"
                     :name "free-text"
                     :type "radio"
                     :default-checked (not (:free-text @quiz-options))
                     :value false
                     :on-click #(dispatch [:quiz-options-filtered {:free-text false}])}]
            [:label {:for "free-text-false"} "Multiple choice"]]]
          [:div "From"
            (for [alphabet @alphabets]
              ^{:key alphabet} [alphabet-input alphabet "from"])]
          [:div "To"
            (for [alphabet @alphabets]
              ^{:key alphabet} [alphabet-input alphabet "to"])]
          [:div
            [:button {:type "button"
                     :on-click #(if (= (:from @quiz-options) (:to @quiz-options))
                                  (js/alert "Invalid selection")
                                  (dispatch [:quiz-started]))}
                     ">>"]]])))

(defn quiz []
  (let [current-char (subscribe [:current-char])
        input-value (subscribe [:input-value])]
    (fn []
      [:div.container
        [:div.char (:hint @current-char)]
        [:input {:type "text"
                 :value @input-value
                 :on-change #(dispatch [:input-value-updated (.-target.value %)])
                 :on-key-press #(when (= 13 (.-which %))
                                  (if (= (.-target.value %) (:solution @current-char))
                                    (dispatch [:right-option-picked])
                                    (dispatch [:wrong-option-picked])))}]
        [:button {:type "button"
                 :on-click #(dispatch [:next-char])}
                 ">>"]])))

(defn main-panel [] 
  (let [panel (subscribe [:panel])]
    (fn []
      (case @panel
          "quiz-options" [quiz-options]
          "quiz" [quiz]))))