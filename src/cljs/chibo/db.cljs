(ns chibo.db
  (:require [cljs.spec :as s]))

(comment
  (s/def ::correct-guesses int?)
  (s/def ::total-guesses int?)
  (s/def ::free-text bool?)
  (s/def ::panel #{"quiz-options" "quiz"})
  (s/def ::from #{"hiragana" "katakana" "romaji"})
  (s/def ::to #{"hiragana" "katakana" "romaji"})
  (s/def ::input (s/keys req-un [::value ::disabled]))
  (s/def ::current-char (s/keys req-un [::hint ::solution]))
  (s/def ::quiz (s/keys req-un [::correct-guesses ::total-guesses ::quiz-type ::free-text ::current-char ::input]))
  (s/def ::db (s/keys req-un [::quiz])))

(def default-db
  {:alphabets ["hiragana" "katakana" "romaji"]
   :panel "quiz-options"
   :quiz {:current-char {}
          :correct-guesses 0
          :input {:value "" :disabled false}
          :total-guesses 0
          :free-text true
          :from "hiragana"
          :to "romaji"}})
