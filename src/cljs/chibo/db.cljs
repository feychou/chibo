(ns chibo.db
  (:require [cljs.spec :as s]))

(def alphabets  {:hiragana {}
                 :katakana {}})

(s/def ::corrent-guesses int?)
(s/def ::incorrect-guesses int?)
(s/def ::level-type #{"to-romaji" "from-romaji" "mixed"})
(s/def ::alphabet (set (keys alphabets)))
(s/def ::quiz (s/keys req-un [::corrent-guesses ::incorrect-guesses ::level-type ::alphabet]))
(s/def ::db (s/keys req-un [::quiz]))

(def default-db
  {:quiz {:alphabet "hiragana"
          :corrent-guesses 0
          :incorrect-guesses 0
          :level-type "mixed"}})
