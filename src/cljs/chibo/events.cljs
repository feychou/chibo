(ns chibo.events
  (:require [re-frame.core :refer [reg-event-db trim-v]]
            [chibo.db :as db]
            [chibo.syllables :refer [syllables]]))

(defn make-char [quiz, random-char]
  {:hint ((keyword (first (:from quiz))) random-char)
   :solution ((keyword (first (:to quiz))) random-char)})

(reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(reg-event-db
  :quiz-options-filtered
  trim-v
  (fn [db [options]]
    (update-in db [:quiz] merge options)))

(reg-event-db
  :quiz-started
  (fn [db _]
    (let [random-char (rand-nth syllables)]
      (update-in (assoc db :panel "quiz") [:quiz] merge {:current-char (make-char (:quiz db) random-char)}))))

(reg-event-db
  :next-char
  (fn [db _]
    (let [random-char (rand-nth syllables)]
      (update-in db [:quiz] merge {:current-char (make-char (:quiz db) random-char)}))))

(reg-event-db
  :quiz-user-input
  trim-v
  (fn [db [value]]
    (let [random-char (rand-nth syllables)
          quiz (:quiz db)]
      (js/console.log value)
      (js/console.log (:solution (:current-char quiz)))
      (if (= value (:solution (:current-char quiz)))
          (update-in db [:quiz] merge {:current-char (make-char quiz random-char)
                                       :correct-guesses (+ (:correct-guesses quiz) 1)
                                       :total-guesses (+ (:total-guesses quiz) 1)})
          (update-in db [:quiz] merge {:current-char (make-char quiz random-char)
                                       :total-guesses (+ (:total-guesses quiz) 1)})))))