(ns chibo.events
  (:require [re-frame.core :refer [reg-event-db trim-v]]
            [chibo.db :as db]))

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
  :panel-changed
  trim-v
  (fn [db [value]]
    (assoc db :panel value)))
