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
  :input
  (fn [db _]
    (:input (:quiz db))))

(reg-sub
  :counter
  (fn [db _]
    (select-keys (:quiz db) [:correct-guesses :total-guesses])))

(reg-sub
  :feedback
  (fn [db _]
    (:feedback (:quiz db))))