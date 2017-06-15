(ns chibo.db
  (:require [cljs.spec :as s]))

(comment
  (s/def ::corrent-guesses int?)
  (s/def ::incorrect-guesses int?)
  (s/def ::free-text bool?)
  (s/def ::quiz-type #{"to-romaji" "from-romaji" "mixed"})
  (s/def ::alphabet #{"hiragana" "katakana"})
  (s/def ::panel #{"alphabet-choice" "quiz-options" "quiz"})
  (s/def ::quiz (s/keys req-un [::corrent-guesses ::incorrect-guesses ::quiz-type ::alphabet]))
  (s/def ::db (s/keys req-un [::quiz]))
)

(def default-db
  {:alphabets ["hiragana" "katakana"]
   :panel "alphabet-choice"
   :quiz {:alphabet "hiragana"
          :corrent-guesses 0
          :incorrect-guesses 0
          :free-text true
          :quiz-type "to-romaji"}})
