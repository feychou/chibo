(ns chibo.events
  (:require [re-frame.core :refer [reg-event-db trim-v]]
            [chibo.db :as db]
            [chibo.syllables :refer [syllables]]))

(reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(reg-event-db
  :alphabet-picked
  trim-v
  (fn [db [alphabet]]
    (update-in db [:quiz] merge {:alphabet alphabet})))

(reg-event-db
  :quiz-options-filtered
  trim-v
  (fn [db [options]]
    (update-in db [:quiz] merge options)))

(reg-event-db
  :quiz-started
  trim-v
  (fn [db _]
    (update-in (assoc db :panel "quiz") [:quiz] merge {:current-char (rand-nth syllables)})))

(reg-event-db
  :next-char
  trim-v
  (fn [db _]
    (let [random-char (rand-nth syllables)
          quiz-char {}]
      (update-in db [:quiz] merge {:current-char (rand-nth syllables)}))))