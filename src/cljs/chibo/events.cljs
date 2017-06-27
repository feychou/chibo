(ns chibo.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx trim-v path]]
            [chibo.db :as db]
            [chibo.syllables :refer [syllables]]))

(def quiz-interceptors [(path :quiz) trim-v])

(defn make-char [quiz, random-char]
  {:hint ((keyword (first (:from quiz))) random-char)
   :solution ((keyword (first (:to quiz))) random-char)})

(reg-event-db
  :initialize-db
  (fn  [_ _]
    db/default-db))

(reg-event-db
  :quiz-options-filtered
  quiz-interceptors
  (fn [quiz [options]]
    (merge quiz options)))

(reg-event-db
  :quiz-started
  (fn [db _]
    (let [random-char (rand-nth syllables)
          choices (if (= (:quiz-type (:quiz db)) "multiple-choice")
                      (take 3 (shuffle syllables))
                      {})]
      (update-in (assoc db :panel "quiz") [:quiz] 
       merge {:current-char (make-char (:quiz db) random-char)
              :choices choices}))))

(reg-event-db
  :input-value-updated
  quiz-interceptors
  (fn [quiz [new-value]]
    (assoc quiz :input {:value new-value
                        :disabled false})))

(reg-event-db
  :panel-changed
  (fn [db _]
    (assoc db :panel "result")))

(reg-event-db
  :new-char
  quiz-interceptors
  (fn [quiz]
    (let [random-char (rand-nth syllables)]
      (assoc quiz :current-char (make-char quiz random-char)))))

(reg-event-db
  :char-skipped
  quiz-interceptors
  (fn [quiz _]
    (let [random-char (rand-nth syllables)]
      (merge quiz {:total-guesses (+ (:total-guesses quiz) 1)
                   :current-char (make-char quiz random-char)}))))

(reg-event-fx
  :feedback-clear
  (fn [{:keys [db]} _]
    {:db (update-in db [:quiz]
          merge {:feedback "off"
                 :input {:value ""
                         :disable false}})
     :dispatch [:new-char]}))

(reg-event-fx
  :wrong-option-picked
  (fn [{:keys [db]} _]
    (let [quiz (:quiz db)]
      {:db (update-in db [:quiz]
            merge {:feedback "wrong"
                   :input {:value ""
                           :disabled true}
                   :total-guesses (+ (:total-guesses quiz) 1)})
       :dispatch-later [{:ms 800 :dispatch [:feedback-clear]}]})))

(reg-event-fx
  :right-option-picked
  (fn [{:keys [db]} _]
    (let [quiz (:quiz db)]
      {:db (update-in db [:quiz]
            merge {:feedback "right"
                   :input {:value ""
                           :disabled true}
                   :correct-guesses (+ (:correct-guesses quiz) 1)
                   :total-guesses (+ (:total-guesses quiz) 1)})
       :dispatch-later [{:ms 800 :dispatch [:feedback-clear]}]})))