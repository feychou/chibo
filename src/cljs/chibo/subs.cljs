(ns chibo.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
  :alphabet
  (fn [db _]
    (:alphabet (:quiz db))))
