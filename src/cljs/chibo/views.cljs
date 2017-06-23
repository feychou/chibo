(ns chibo.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :refer [atom dom-node]]))

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
      [:div.panel-container
        [:h2 "Home"]
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

(def focus-wrapper 
  (with-meta identity
    {:component-did-update #(.focus (dom-node %))}))

(defn on-skip []
  (.focus (.getElementById js/document "solution-input"))
  (dispatch [:skip-char]))

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
                                  (if (= (.-target.value %) (:solution @current-char))
                                    (dispatch [:right-option-picked])
                                    (dispatch [:wrong-option-picked])))}])]))

(defn quiz []
  (let [current-char (subscribe [:current-char])
        counter (subscribe [:counter])
        feedback (subscribe [:feedback])]
    (fn []
      [:div.panel-container
        [:h2 "Quiz"]
        [:div.counter (str (:correct-guesses @counter) "/" (:total-guesses @counter))]
        [:div.char (:hint @current-char)]
        [solution-input]
        [:div.feedback (when (not= @feedback "off") @feedback)]
        [:button {:type "button"
                 :on-click #(on-skip)}
                 ">>"]
        [:button {:type "button"
                 :on-click #(dispatch [:panel-changed "result"])}
                 "Finish"]])))

(defn result []
  (let [counter (subscribe [:counter])]
  (fn []
    [:div.panel-container
      [:h2 "Result"]
      [:div (str "Congrats! You got " (:correct-guesses @counter) " out of " (:total-guesses @counter) " right.")]
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