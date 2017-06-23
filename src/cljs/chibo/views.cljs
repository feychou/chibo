(ns chibo.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :refer [atom dom-node]]
            [clojure.string :refer [replace capitalize]]))

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

(defn quiz-type-input [type]
  (let [quiz-options (subscribe [:quiz-options])]
    (fn []
      [:span
        [:input {:id type
                 :name "quiz-type"
                 :type "radio"
                 :default-checked (= (:quiz-type @quiz-options) type)
                 :on-click #(dispatch [:quiz-options-filtered {:quiz-type type}])}]
        [:label {:for type} (capitalize (replace type #"-" " "))]])))

(defn quiz-options []
  (let [quiz-options (subscribe [:quiz-options])
        alphabets (subscribe [:alphabets])
        quiz-types (subscribe [:quiz-types])]
    (fn []
      [:div.panel-container
        [:h2 "Home"]
        [:form
          [:div
            (for [quiz-type @quiz-types]
              ^{:key quiz-type} [quiz-type-input quiz-type])]
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
                     ">>"]]]])))

(def focus-wrapper 
  (with-meta identity
    {:component-did-update #(.focus (dom-node %))}))

(defn on-skip []
  (.focus (.getElementById js/document "solution-input"))
  (dispatch [:char-skipped]))

(defn on-submit [input-value solution]
  (if (= input-value solution)
    ((dispatch [:right-option-picked]))
    (dispatch [:wrong-option-picked])))

(defn solution-input []
  (let [current-char (subscribe [:current-char])
        input (subscribe [:input])]
    [focus-wrapper
      (fn []
        [:input {:type "text"
                 :id "solution-input"
                 :auto-focus true
                 :value (:value @input)
                 :disabled (:disabled @input)
                 :on-change #(dispatch [:input-value-updated (.-target.value %)])
                 :on-key-press #(when (= 13 (.-which %))
                                  (on-submit (:value @input) (:solution @current-char)))}])]))

(defn quiz []
  (let [current-char (subscribe [:current-char])
        input (subscribe [:input])
        counter (subscribe [:counter])
        feedback (subscribe [:feedback])
        quiz-type (subscribe [:quiz-type])]
    (fn []
      [:div.panel-container
        [:h2 "Quiz"]
        [:div.counter (str (:correct-guesses @counter) "/" (:total-guesses @counter))]
        [:div.char (:hint @current-char)]
        (if (= @quiz-type "free-text")
          (do
            [:span
              [solution-input]
              [:span.feedback (when (not= @feedback "off") @feedback)]
              [:button {:type "button"
                       :on-click #(on-submit (:value @input) (:solution @current-char))}
                       "Submit"]
              [:button {:type "button"
                       :on-click #(on-skip)}
                       "Skip"]])
          (do
            [:h3 "Multiple choice!"]))
        [:button {:type "button"
                 :on-click #(dispatch [:panel-changed "result"])}
                 "Finish"]])))

(defn result []
  (let [counter (subscribe [:counter])]
  (fn []
    [:div.panel-container
      [:h2 "Result"]
      [:div (str "Congrats! You got "
                 (:correct-guesses @counter)
                 " out of "
                 (:total-guesses @counter) " right.")]
      [:button {:type "button"
               :on-click #(dispatch [:initialize-db])}
               "Start over"]])))

(defn main-panel [] 
  (let [panel (subscribe [:panel])]
    (fn []
      [:div.app-container
        [:h1 "chibo"]
        [#(case @panel
            "quiz-options" [quiz-options]
            "quiz" [quiz]
            "result" [result])]])))