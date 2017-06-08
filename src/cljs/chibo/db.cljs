(ns chibo.db
  (:require [cljs.spec :as s]))

(comment
  (s/def ::corrent-guesses int?)
  (s/def ::incorrect-guesses int?)
  (s/def ::level-type #{"to-romaji" "from-romaji" "mixed"})
  (s/def ::alphabet #{"hiragana" "katakana"})
  (s/def ::panel #{"alphabet-choice" "type-choice" "quiz"})
  (s/def ::quiz (s/keys req-un [::corrent-guesses ::incorrect-guesses ::level-type ::alphabet]))
  (s/def ::db (s/keys req-un [::quiz]))
)

(def default-db
  {:alphabets ["hiragana" "katakana"]
   :panel "alphabet-choice"
   :quiz {:alphabet "hiragana"
          :corrent-guesses 0
          :incorrect-guesses 0
          :level-type "mixed"}})
