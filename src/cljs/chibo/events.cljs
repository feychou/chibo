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
    (let [random-char (rand-nth syllables)]
      (update-in (assoc db :panel "quiz") [:quiz] 
       merge {:current-char (make-char (:quiz db) random-char)}))))

(reg-event-db
  :next-char
  quiz-interceptors
  (fn [quiz _]
    (let [random-char (rand-nth syllables)]
      (merge quiz {:total-guesses (+ (:total-guesses quiz) 1)
                   :current-char (make-char quiz random-char)}))))

(reg-event-db
  :input-value-updated
  quiz-interceptors
  (fn [quiz [new-value]]
    (merge quiz {:input {:value new-value
                         :disabled false}})))

(reg-event-fx
  :wrong-option-picked
  (fn [{:keys [db]} _]
    (let [random-char (rand-nth syllables)
          quiz (:quiz db)]
      {:db (update-in db [:quiz]
            merge {:input {:value "" :disabled false}
                   :feedback "wrong"
                   :current-char (make-char quiz random-char)
                   :total-guesses (+ (:total-guesses quiz) 1)})})))

(reg-event-fx
  :right-option-picked
  (fn [{:keys [db]} _]
    (let [random-char (rand-nth syllables)
          quiz (:quiz db)]
      {:db (update-in db [:quiz]
            merge {:input {:value "" :disabled false}
                   :feedback "right"
                   :current-char (make-char quiz random-char)
                   :correct-guesses (+ (:correct-guesses quiz) 1)
                   :total-guesses (+ (:total-guesses quiz) 1)})})))