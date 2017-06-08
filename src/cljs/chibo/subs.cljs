(ns chibo.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
  :alphabets
  (fn [db _]
    (:alphabets db)))

(reg-sub
  :panel
  (fn [db _]
    (:panel db)))