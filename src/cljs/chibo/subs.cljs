(ns chibo.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :quiz-options
  (fn [db _]
    (select-keys (:quiz db) [:quiz-type :free-text])))

(reg-sub
  :alphabets
  (fn [db _]
    (:alphabets db)))

(reg-sub
  :panel
  (fn [db _]
    (:panel db)))