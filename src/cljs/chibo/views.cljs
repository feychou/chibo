(ns chibo.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :refer [atom dom-node]]
            [clojure.string :refer [replace capitalize]]))

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

(defn free-text-mode []
  (let [feedback (subscribe [:feedback])
        input (subscribe [:input])
        current-char (subscribe [:current-char])]
    (fn []
      [:span
        [solution-input]
        [:span.feedback (when (not= @feedback "off") @feedback)]
        [:button.control {:type "button"
                          :on-click #(on-submit (:value @input) (:solution @current-char))}
                         "Submit"]
        [:button.control {:type "button"
                          :on-click #(on-skip)}
                         "Skip"]])))

(defn quiz []
  (let [current-char (subscribe [:current-char])
        counter (subscribe [:counter])
        quiz-type (subscribe [:quiz-type])]
    (fn []
      [:div.quiz-free-text-container
        [:div.char (:hint @current-char)]
        (if (= @quiz-type "free-text")
          [free-text-mode]
          (do
            [:h3 "Multiple choice!"]))
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