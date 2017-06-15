(ns chibo.events
  (:require [re-frame.core :refer [reg-event-db]]
            [chibo.db :as db]))

(reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(reg-event-db
  :alphabet-picked
  (fn [db [alphabet]]
    (assoc db :alphabet alphabet :panel "options"))) 
