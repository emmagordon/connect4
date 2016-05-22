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

(def test-win-on-final-move [[x x o o o o]
                             [o x o o o x]
                             [x o x x x o]
                             [o x o o o x]
                             [x o x x x o]
                             [o x o o o x]
                             [x o x x x o]])

(deftest test-get-pos
  (testing "empty"
    (is (= (get-pos [3 2] test-win-diag2) .)))
  (testing "x"
    (is (= (get-pos [0 4] test-win-diag2) x)))
  (testing "o"
    (is (= (get-pos [4 2] test-win-diag2) o))))

(deftest test-get-col
  (testing "get-col"
    (is (= (get-col 4 test-win-col)
           [o o x x x x]))))

(deftest move-validity
  (testing "invalid"
    (is (not (valid-move? 4 test-win-col))))
  (testing "valid"
    (is (valid-move? 3 test-win-col))))

(deftest winning
  (testing "not yet"
    (is (= (winner init-board) nil)))
  (testing "row"
    (is (= (winner test-win-row) o)))
  (testing "col"
    (is (= (winner test-win-col) x)))
  (testing "diag1"
    (is (= (winner test-win-diag1) x)))
  (testing "diag2"
    (is (= (winner test-win-diag2) o))))

(deftest brice-wins
  (testing "only a solved problem if you remember the solution"
    (is (= (winner test-emmas-shame) x))))

(deftest draw
  (testing "full board"
    (is (not (can-move? test-full-board))))
  (testing "no winner"
    (is (= (winner test-full-board) nil)))
  (testing "not a draw"
    (is (= (winner test-win-on-final-move) o))))
