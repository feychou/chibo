(ns chibo.db
  (:require [cljs.spec :as s]))

(comment
  (s/def ::corrent-guesses int?)
  (s/def ::total-guesses int?)
  (s/def ::free-text bool?)
  (s/def ::panel #{"quiz-options" "quiz"})
  (s/def ::from #{"hiragana" "katakana" "romaji"})
  (s/def ::to #{"hiragana" "katakana" "romaji"})
  (s/def ::current-char (s/keys req-un [::hint ::solution]))
  (s/def ::quiz (s/keys req-un [::corrent-guesses ::total-guesses ::quiz-type ::free-text ::current-char]))
  (s/def ::db (s/keys req-un [::quiz])))

(def default-db
  {:alphabets ["hiragana" "katakana" "romaji"]
   :panel "quiz-options"
   :quiz {:current-char {}
          :corrent-guesses 0
          :total-guesses 0
          :free-text true
          :from "hiragana"
          :to "romaji"}})
