(ns chibo.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :refer [atom dom-node]]
            [clojure.string :refer [replace capitalize]]))

(defn on-submit [input-value solution]
  (if (= input-value solution)
    (dispatch [:right-option-picked])
    (dispatch [:wrong-option-picked])))

(def focus-wrapper 
  (with-meta identity
    {:component-did-update #(.focus (dom-node %))}))

(defn on-skip [quiz-type]
  (when (= quiz-type "free-text")
    (.focus (.getElementById js/document "solution-input")))
  (dispatch [:char-skipped]))

(defn alphabet-input [alphabet, input-name]
  (let [quiz-options (subscribe [:quiz-options])
        id (str input-name "-" alphabet)]
    (fn []
      [:span.input-wrapper
        [:input {:id id
                 :name input-name
                 :type "radio"
                 :default-checked (= ((keyword input-name) @quiz-options) alphabet)
                 :on-click #(dispatch [:quiz-options-filtered {(keyword input-name) alphabet}])
                 :value true}]
        [:label {:for id}
          [:span (capitalize alphabet)]]])))

(defn quiz-type-input [type]
  (let [quiz-options (subscribe [:quiz-options])]
    (fn []
      [:span.input-wrapper
        [:input {:id type
                 :name "quiz-type"
                 :type "radio"
                 :default-checked (= (:quiz-type @quiz-options) type)
                 :on-click #(dispatch [:quiz-options-filtered {:quiz-type type}])}]
        [:label {:for type} 
          [:span (capitalize (replace type #"-" " "))]]])))

(defn char-choice-input [char]
  (let [current-char (subscribe [:current-char])
        feedback (subscribe [:feedback])]
    (fn []
      [:span.input-wrapper.choice
        [:input {:id char
                 :name "char-choice"
                 :type "radio"
                 :disabled (not= @feedback "off")
                 :on-click #(on-submit (.-target.id %) (:solution @current-char))}]
        [:label {:for char}
          [:span char]]])))


(defn quiz-options []
  (let [quiz-options (subscribe [:quiz-options])
        alphabets (subscribe [:alphabets])
        quiz-types (subscribe [:quiz-types])]
    (fn []
      [:div.panel-container
        [:form
          [:h1 "chibo"]
          [:div.options-container
            [:div
              (for [quiz-type @quiz-types]
                ^{:key quiz-type} [quiz-type-input quiz-type])]
            [:div [:b.group-label "from"]
              (for [alphabet @alphabets]
                ^{:key alphabet} [alphabet-input alphabet "from"])]
            [:div [:b.group-label "to"]
              (for [alphabet @alphabets]
                ^{:key alphabet} [alphabet-input alphabet "to"])]
            [:div
              [:button.go-button {:type "button"
                                  :on-click #(if (= (:from @quiz-options) (:to @quiz-options))
                                                (js/alert "Invalid selection")
                                                (dispatch [:quiz-started]))}
                "Go!"]]]]])))

(defn solution-input []
  (let [current-char (subscribe [:current-char])
        input (subscribe [:input])
        feedback (subscribe [:feedback])]
    [focus-wrapper
      (fn []
        [:input {:type "text"
                 :class-name @feedback
                 :id "solution-input"
                 :auto-focus true
                 :value (:value @input)
                 :disabled (:disabled @input)
                 :on-change #(dispatch [:input-value-updated (.-target.value %)])
                 :on-key-press #(when (= 13 (.-which %))
                                  (on-submit (:value @input) (:solution @current-char)))}])]))

(defn free-text-mode []
  (let [input (subscribe [:input])
        current-char (subscribe [:current-char])]
    (fn []
      [:span
        [:div.char (:hint @current-char)]
        [:span
          [solution-input]
          [:button.control {:type "button"
                            :on-click #(on-submit (:value @input) (:solution @current-char))}
                           "Submit"]]])))

(defn multiple-choice-mode []
  (let [choices (subscribe [:choices])
        current-char (subscribe [:current-char])
        feedback (subscribe [:feedback])]
    (fn []
      [:span
        [:div.char.multiple-choice-char (:hint @current-char)
          (when (= @feedback "wrong") [:span.feedback.wrong "✘"])
          (when (= @feedback "right") [:span.feedback.right "✔"])]
        [:div.choices
          [:div.choice-row
            (for [choice (take 2 @choices)]
              ^{:key choice} [char-choice-input choice])]
          [:div.choice-row
            (for [choice (take-last 2 @choices)]
              ^{:key choice} [char-choice-input choice])]]])))

(defn quiz []
  (let [counter (subscribe [:counter])
        quiz-type (subscribe [:quiz-type])]
    (fn []
      [:div.quiz-free-text-container
        (if (= @quiz-type "free-text")
          [free-text-mode]
          [multiple-choice-mode])
        [:button.control {:type "button"
                          :on-click #(on-skip quiz-type)}
                         "Skip"]
        [:button.control {:type "button"
                          :on-click #(dispatch [:panel-changed "result"])}
                         "Finish"]
        [:div.counter (str (:correct-guesses @counter) "/" (:total-guesses @counter))]])))

(defn result []
  (let [counter (subscribe [:counter])]
  (fn []
    [:div.result-container
      [:div "Congrats!"
            [:br]
            "You got "
            [:b (:correct-guesses @counter)]
            " right out of "
            [:b (:total-guesses @counter)]]
      [:button.control {:type "button"
                        :on-click #(dispatch [:initialize-db])}
                       "Start over"]])))

(defn main-panel [] 
  (let [panel (subscribe [:panel])]
    (fn []
      [:div.app-container
        [:div.chibi-container]
        [#(case @panel
            "quiz-options" [quiz-options]
            "quiz" [quiz]
            "result" [result])]])))