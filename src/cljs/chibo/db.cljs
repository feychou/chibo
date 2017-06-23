(ns chibo.db
  (:require [cljs.spec :as s]))

(comment
  (s/def ::correct-guesses int?)
  (s/def ::total-guesses int?)
  (s/def ::quiz-type #{"multiple-choice" "free-text"})
  (s/def ::panel #{"quiz-options" "quiz" "result"})
  (s/def ::from #{"hiragana" "katakana" "romaji"})
  (s/def ::to #{"hiragana" "katakana" "romaji"})
  (s/def ::input (s/keys req-un [::value ::disabled]))
  (s/def ::current-char (s/keys req-un [::hint ::solution]))
  (s/def ::feedback #{"wrong" "right" "off"})
  (s/def ::quiz (s/keys req-un [::correct-guesses ::total-guesses ::quiz-type ::quiz-type ::current-char ::input]))
  (s/def ::db (s/keys req-un [::quiz])))

(def default-db
  {:alphabets ["hiragana" "katakana" "romaji"]
   :quiz-types ["free-text" "multiple-choice"]
   :panel "quiz-options"
   :quiz {:current-char {}
          :correct-guesses 0
          :feedback "off"
          :input {:value "" :disabled false}
          :total-guesses 0
          :quiz-type "free-text"
          :from "hiragana"
          :to "romaji"}})
