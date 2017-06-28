(ns chibo.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx trim-v path]]
            [chibo.db :as db]
            [chibo.syllables :refer [syllables]]))

(def quiz-interceptors [(path :quiz) trim-v])

(defn make-char [quiz current-char]
  {:hint ((keyword (first (:from quiz))) current-char)
   :solution ((keyword (first (:to quiz))) current-char)})

(defn make-picks [quiz-type current-char]
  (if (= quiz-type "multiple-choice")
    (take 3 (shuffle (remove #(= (:r current-char) (:r %)) syllables)))
    []))

(defn make-choices [picks current-char]
  (shuffle
    (conj (reduce #(conj %1 (:r %2)) [] picks)
          (:r current-char))))

(defn make-random-char []
  (rand-nth (shuffle syllables)))

(reg-event-db
  :initialize-db
  (fn  [_ _]
    db/default-db))

(reg-event-db
  :quiz-options-filtered
  quiz-interceptors
  (fn [quiz [options]]
    (merge quiz options)))

(reg-event-db
  :quiz-started
  (fn [db _]
    (let [current-char (make-random-char)
          picks (make-picks (:quiz-type (:quiz db)) current-char)]
      (update-in (assoc db :panel "quiz") [:quiz] 
       merge {:current-char (make-char (:quiz db) current-char)
              :choices (make-choices picks current-char)}))))

(reg-event-db
  :input-value-updated
  quiz-interceptors
  (fn [quiz [new-value]]
    (assoc quiz :input {:value new-value
                        :disabled false})))

(reg-event-db
  :panel-changed
  (fn [db _]
    (assoc db :panel "result")))

(reg-event-db
  :new-char
  quiz-interceptors
  (fn [quiz]
    (let [current-char (make-random-char)
          picks (make-picks (:quiz-type quiz) current-char)]
      (assoc quiz :current-char (make-char quiz current-char)
                  :choices (make-choices picks current-char)))))

(reg-event-db
  :char-skipped
  quiz-interceptors
  (fn [quiz _]
    (let [current-char (make-random-char)
          picks (make-picks (:quiz-type quiz) current-char)]
      (merge quiz {:total-guesses (+ (:total-guesses quiz) 1)
                   :current-char (make-char quiz current-char)
                   :choices (make-choices picks current-char)}))))

(reg-event-fx
  :feedback-clear
  (fn [{:keys [db]} _]
    {:db (update-in db [:quiz]
          merge {:feedback "off"
                 :input {:value ""
                         :disable false}})
     :dispatch [:new-char]}))

(reg-event-fx
  :wrong-option-picked
  (fn [{:keys [db]} _]
    (let [quiz (:quiz db)
          input-value (:value (:input quiz))]
      {:db (update-in db [:quiz]
            merge {:feedback "wrong"
                   :input {:value input-value
                           :disabled true}
                   :total-guesses (+ (:total-guesses quiz) 1)})
       :dispatch-later [{:ms 800 :dispatch [:feedback-clear]}]})))

(reg-event-fx
  :right-option-picked
  (fn [{:keys [db]} _]
    (let [quiz (:quiz db)
          input-value (:value (:input quiz))]
      {:db (update-in db [:quiz]
            merge {:feedback "right"
                   :input {:value input-value
                           :disabled true}
                   :correct-guesses (+ (:correct-guesses quiz) 1)
                   :total-guesses (+ (:total-guesses quiz) 1)})
       :dispatch-later [{:ms 800 :dispatch [:feedback-clear]}]})))