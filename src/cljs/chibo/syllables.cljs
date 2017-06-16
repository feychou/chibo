(ns chibo.syllables)

(def syllables [
  {:h "ん" :k "ン" :r "n'"}

  {:h "か" :k "カ" :r "ka"} {:h "き" :k "キ" :r "ki"} {:h "く" :k "ク" :r "ku"}
  {:h "け" :k "ケ" :r "ke"} {:h "こ" :k "コ" :r "ko"}

  {:h "さ" :k "サ" :r "sa"} {:h "し" :k "シ" :r "shi" :ks "si"}
  {:h "す" :k "ス" :r "su"} {:h "せ" :k "セ" :r "se"} {:h "そ" :k "ソ" :r "so"}

  {:h "た" :k "タ" :r "ta"} {:h "ち" :k "チ" :r "chi" :ks "ti"}
  {:h "つ" :k "ツ" :r "tsu" :ks "tu"} {:h "て" :k "テ" :r "te"}
  {:h "と" :k "ト" :r "to"}

  {:h "な" :k "ナ" :r "na"} {:h "に" :k "ニ" :r "ni"} {:h "ぬ" :k "ヌ" :r "nu"}
  {:h "ね" :k "ネ" :r "ne"} {:h "の" :k "ノ" :r "no"}

  {:h "は" :k "ハ" :r "ha"} {:h "ひ" :k "ヒ" :r "hi"}
  {:h "ふ" :k "フ" :r "fu" :ks "hu"} {:h "へ" :k "ヘ" :r "he"}
  {:h "ほ" :k "ホ" :r "ho"}

  {:h "ま" :k "マ" :r "ma"} {:h "み" :k "ミ" :r "mi"} {:h "む" :k "ム" :r "mu"}
  {:h "め" :k "メ" :r "me"} {:h "も" :k "モ" :r "mo"}

  {:h "や" :k "ヤ" :r "ya"} {:h "ゆ" :k "ユ" :r "yu"} {:h "よ" :k "ヨ" :r "yo"}
  {:h "ら" :k "ラ" :r "ra"} {:h "り" :k "リ" :r "ri"} {:h "る" :k "ル" :r "ru"}
  {:h "れ" :k "レ" :r "re"} {:h "ろ" :k "ロ" :r "ro"}

  {:h "わ" :k "ワ" :r "wa"} {:h "ゐ" :k "ヰ" :r "wi" :ks "i"}
  {:h "ゑ" :k "ヱ" :r "we" :ks "e"} {:h "を" :k "ヲ" :r "o" :ks "o"}

  ; dakuten no gojūon
  {:h "が" :k "ガ" :r "ga"} {:h "ぎ" :k "ギ" :r "gi"} {:h "ぐ" :k "グ" :r "gu"}
  {:h "げ" :k "ゲ" :r "ge"} {:h "ご" :k "ゴ" :r "go"}

  {:h "ざ" :k "ザ" :r "za"} {:h "じ" :k "ジ" :r "ji" :ks "zi"}
  {:h "ず" :k "ズ" :r "zu"} {:h "ぜ" :k "ゼ" :r "ze"} {:h "ぞ" :k "ゾ" :r "zo"}

  {:h "だ" :k "ダ" :r "da"} {:h "ぢ" :k "ヂ" :r "ji" :ks "zi"}
  {:h "づ" :k "ヅ" :r "zu"} {:h "で" :k "デ" :r "de"} {:h "ど" :k "ド" :r "do"}

  {:h "ば" :k "バ" :r "ba"} {:h "び" :k "ビ" :r "bi"} {:h "ぶ" :k "ブ" :r "bu"}
  {:h "べ" :k "ベ" :r "be"} {:h "ぼ" :k "ボ" :r "bo"}

  {:h "ぱ" :k "パ" :r "pa"} {:h "ぴ" :k "ピ" :r "pi"} {:h "ぷ" :k "プ" :r "pu"}
  {:h "ぺ" :k "ペ" :r "pe"} {:h "ぽ" :k "ポ" :r "po"}

  ; yōon
  {:h "きゃ" :k "キャ" :r "kya"} {:h "きゅ" :k "キュ" :r "kyu"}
  {:h "きょ" :k "キョ" :r "kyo"}

  {:h "しゃ" :k "シャ" :r "sha" :ks "sya"}
  {:h "しゅ" :k "シュ" :r "shu" :ks "syu"}
  {:h "しょ" :k "ショ" :r "sho" :ks "syo"}

  {:h "ちゃ" :k "チャ" :r "cha" :ks "tya"}
  {:h "ちゅ" :k "チュ" :r "chu" :ks "tyu"}
  {:h "ちょ" :k "チョ" :r "cho" :ks "tyo"}

  {:h "にゃ" :k "ニャ" :r "nya"} {:h "にゅ" :k "ニュ" :r "nyu"}
  {:h "にょ" :k "ニョ" :r "nyo"}

  {:h "ひゃ" :k "ヒャ" :r "hya"} {:h "ひゅ" :k "ヒュ" :r "hyu"}
  {:h "ひょ" :k "ヒョ" :r "hyo"}

  {:h "みゃ" :k "ミャ" :r "mya"} {:h "みゅ" :k "ミュ" :r "myu"}
  {:h "みょ" :k "ミョ" :r "myo"}

  {:h "りゃ" :k "リャ" :r "rya"} {:h "りゅ" :k "リュ" :r "ryu"}
  {:h "りょ" :k "リョ" :r "ryo"}

  ; dakuten no yōon
  {:h "ぎゃ" :k "ギャ" :r "gya"} {:h "ぎゅ" :k "ギュ" :r "gyu"}
  {:h "ぎょ" :k "ギョ" :r "gyo"}

  {:h "じゃ" :k "ジャ" :r "ja" :ks "zya"} {:h "じゅ" :k "ジュ" :r "ju" :ks "zyu"}
  {:h "じょ" :k "ジョ" :r "jo" :ks "zyo"}

  ; order counts
  {:h "ぢゃ" :k "ヂャ" :r "ja" :ks "zya"}
  {:h "ぢゅ" :k "ヂュ" :r "ju" :ks "zyu"}
  {:h "ぢょ" :k "ヂョ" :r "jo" :ks "zyo"}

  {:h "びゃ" :k "ビャ" :r "bya"} {:h "びゅ" :k "ビュ" :r "byu"}
  {:h "びょ" :k "ビョ" :r "byo"}

  {:h "ぴゃ" :k "ピャ" :r "pya"} {:h "ぴゅ" :k "ぴュ" :r "pyu"}
  {:h "ぴょ" :k "ピョ" :r "pyo"}])
