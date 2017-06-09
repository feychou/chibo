(ns chibo.events
  (:require [re-frame.core :as rf]
            [chibo.db :as db]))

(rf/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-db
  :alphabet-picked
  (fn [db [alphabet]]
    (assoc db :alphabet alphabet :panel "quiz"))) 
