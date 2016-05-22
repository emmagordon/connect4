(ns connect4.core
  (:gen-class))

(def . ".") ; empty grid space
(def x "x") ; red piece
(def o "o") ; blue piece

; A vector of column vectors - i.e. left is down, up is left...
; (this is less insane than it sounds - most of the operations in a game
; game of connect4 are concerned with columns, not rows, so it honestly
; makes sense as an internal representation of the board!)
(def init-board [[. . . . . .]
                 [. . . . . .]
                 [. . . . . .]
                 [. . . . . .]
                 [. . . . . .]
                 [. . . . . .]
                 [. . . . . .]])

(defn print-board [board]
  "Prints a user-friendly representation of the game board,
   with down, left etc. returned to their conventional directions!"
  (let [rows (reverse (apply map vector board))
        col-idxs (apply str (interleave (repeat " ")
                                        (range (count board))))
        output-width (inc (count col-idxs))]
    (println (apply str (repeat output-width "=")))
    (doseq [row rows] (println row))
    (println col-idxs)
    (println (apply str (repeat output-width "=")))))

(defn get-pos [[col row] board]
  "Returns the piece in the given position on the board."
  (get-in board [col row]))

(defn get-col [col board]
  "Returns a list of pieces in the given column of the board,
   where the first element is the piece in the bottom row."
  (nth board col))

(defn pad-to-n [line pad-char n]
  "Pads the given line to length n."
  (concat line (repeat (- n (count line)) pad-char)))

(defn get-diags [board]
  "Returns a list of all diagonal lines on the board, where each
   is a list of pieces on that line."
  (defn get-some-diags [board]
    "Gets diagonals at a 45 degree angle of inclination from the
     bottom of the game board."
    (let [get-cols (fn [op] (map (fn [[i xs]] (op i xs)) (map-indexed vector board)))
          pad-cols (fn [cols] (map #(pad-to-n % . (count (first board))) cols))
          upper-left-cols (pad-cols (get-cols drop))
          lower-right-cols (pad-cols (map reverse (get-cols take)))
          upper-left (apply map vector upper-left-cols)
          lower-right (apply map vector lower-right-cols)]
      (concat upper-left lower-right)))
  ; There are 2 sets of diagonals, which cross each other. We can get both by calling
  ; get-some-diags twice, once with the board and once with a mirror image of the board.
  (mapcat get-some-diags [board (reverse board)]))

(defn get-winner [line]
  "Returns the player with 4 consecutive pieces in the given line,
   otherwise returns nil."
  (let [groups (partition-by identity line)
        winner (ffirst (filter #(and (not= . (first %)) (>= (count %) 4)) groups))]
    winner))

(defn winner [board]
  "Returns the player with 4 consective pieces in a horizontal,
   vertical or diagonal line anywhere on the board, or nil if
   this condition is not met."
  (let [columns board
        rows (apply map vector board)
        diags (get-diags board)]
    (some (fn [line] (#{x o} (get-winner line))) (concat columns rows diags))))

(defn display [board msg]
  "Displays the game board and a message to the players."
  (do (print-board board) (println msg)))

(defn prompt-move [player board]
  "Prompts for the player's chosen move and returns their input."
  (let [prompt (str "Player " player " select column to place piece: ")]
    (do
      (display board prompt)
      (flush)
      (read-line))))

(defn valid-move? [col board]
  "Tests if there's space in the given column to place a piece."
  (= . (last (get-col col board))))

(defn get-move [player board]
  "Repeatedly prompts the player for a move until they make a valid
   selection. Returns the column in which they wish to palce a piece."
  (try
    (loop []
      (let [col (Integer. (prompt-move player board))]
        (if (valid-move? col board)
          col
          (do (println "Invalid move") (recur)))))
  (catch Exception e
    (do
      (println "That was not a valid choice")
      (get-move player board)))))

(defn make-move [col board player]
  "Returns a new board with the player's piece added to the first
   empty row in the selected column."
  (let [row (.indexOf (map #(= . %) (get-col col board)) true)
        new-board (assoc-in board [col row] player)]
    new-board))

(defn can-move? [board]
  "Returns true if there are any available moves."
  (some #(valid-move? % board) (range (count board))))

(defn swap-player [current-player]
  "Switches the active player."
  (if (= current-player x) o x))

(defn game-loop []
  "Plays a game of connect4."
  (loop [board init-board
         player o]
    (let [new-board (make-move (get-move player board) board player)]
      (cond
        (winner new-board) (display new-board (str "Player " player " wins!"))
        (not (can-move? new-board)) (display new-board "It's a draw!")
        :else (recur new-board (swap-player player))))))

(defn -main []
  (game-loop))
