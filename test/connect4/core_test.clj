(ns connect4.core-test
  (:require [clojure.test :refer :all])
  (:use connect4.core :reload))

(def test-win-col [[o . . . . .]
                   [x . . . . .]
                   [x . . o . .]
                   [x . x . . .]
                   [o o x x x x]
                   [. . . . . .]
                   [. . . . . .]])

(def test-win-row [[o . . . . .]
                   [x . . . . .]
                   [o . . . . .]
                   [o x . . . .]
                   [o . . . . .]
                   [o . . . . .]
                   [. . . . . .]])

(def test-win-diag1 [[o . . . x .]
                     [x . . x . .]
                     [o . x . . .]
                     [x x . . . .]
                     [o . o . . .]
                     [o . . o . .]
                     [. . . . . .]])

(def test-win-diag2 [[o . . . x .]
                     [x . . x . .]
                     [o . x . . .]
                     [x o . . . .]
                     [o . o . . .]
                     [o . . o . .]
                     [. . . . . .]])

(def test-emmas-shame [[o . . . . .]
                       [x x o . . .]
                       [x o x o x .]
                       [o x o x . .]
                       [o o x o . .]
                       [x x . . . .]
                       [o . . . . .]])

(def test-full-board [[x o x x x o]
                      [o x o o o x]
                      [x o x x x o]
                      [o x o o o x]
                      [x o x x x o]
                      [o x o o o x]
                      [x o x x x o]])

(def test-win-on-final-move [[x x o o o .]
                             [o x o o o x]
                             [x o x x x o]
                             [o x o o o x]
                             [x o x x x o]
                             [o x o o o x]
                             [x o x x x o]])

(deftest invalid-move
  (testing "Invalid move"
    (is (not (valid-move? 4 test-win-col)))))

(deftest valid-move
  (testing "Valid move"
    (is (valid-move? 3 test-win-col))))

(deftest test-get-col
  (testing "get-col"
    (is (= (get-col 4 test-win-col)
           [o o x x x x]))))

(deftest test-get-pos
  (testing "get-pos"
    (is (= (get-pos [2 3] test-win-diag2) .))))

(deftest no-winner-yet
  (testing "no winner yet"
    (is (= (winner init-board) nil))))

(deftest winning-row
  (testing "winning row"
    (is (= (winner test-win-row) o))))

(deftest winning-col
  (testing "winning col"
    (is (= (winner test-win-col) x))))

(deftest winning-diag1
  (testing "winning diag1"
    (is (= (winner test-win-diag1) x))))

(deftest winning-diag2
  (testing "winning diag2"
    (is (= (winner test-win-diag2) o))))

(deftest brice-wins
  (testing "only a solved problem if you remember the solution"
    (is (= (winner test-emmas-shame) x))))

(deftest no-moves
  (testing "full board"
    (is (not (can-move? test-full-board)))))

(deftest draw
  (testing "no moves left"
    (is (= (winner test-full-board) nil))))
