(ns chibo.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :quiz-options
  (fn [db _]
    (select-keys (:quiz db) [:from :to :free-text])))

(reg-sub
  :alphabets
  (fn [db _]
    (:alphabets db)))

(reg-sub
  :panel
  (fn [db _]
    (:panel db)))

(reg-sub
  :current-char
  (fn [db _]
    (:current-char (:quiz db))))

(reg-sub
  :input-value
  (fn [db _]
    (:input-value (:quiz db))))