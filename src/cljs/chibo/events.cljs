(ns chibo.events
  (:require [re-frame.core :refer [reg-event-db trim-v]]
            [chibo.db :as db]
            [chibo.syllables :refer [syllables]]))

(defn make-char [alphabet, random-char]
  {:hint ((keyword (first alphabet)) random-char)
   :solution ((keyword (first alphabet)) random-char)})

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
  trim-v
  (fn [db _]
    (let [random-char (rand-nth syllables)]
      (update-in (assoc db :panel "quiz") [:quiz] merge {:current-char (make-char (:from (:quiz db)) random-char)}))))

(reg-event-db
  :next-char
  trim-v
  (fn [db _]
    (let [random-char (rand-nth syllables)]
      (update-in db [:quiz] merge {:current-char (make-char (:from (:quiz db)) random-char)}))))