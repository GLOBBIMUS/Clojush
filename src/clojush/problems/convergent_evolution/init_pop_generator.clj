;; syllables.clj
;; Tom Helmuth, thelmuth@cs.umass.edu
;;
;; Problem Source:
;;   C. Le Goues et al., "The ManyBugs and IntroClass Benchmarks for Automated Repair of C Programs,"
;;   in IEEE Transactions on Software Engineering, vol. 41, no. 12, pp. 1236-1256, Dec. 1 2015.
;;   doi: 10.1109/TSE.2015.2454513
;;
;; Given a string (max length 20, containing symbols, spaces, digits, and
;; lowercase letters), count the number of occurrences of vowels (a,e,i,o,u,y)
;; in the string and print that number as X in "The number of syllables is X"
;;
;; input stack has the input string

;; These should work for:
;;   * syllables
;;   * replace-space-with-newline
;;   * checksum

(ns clojush.problems.convergent-evolution.init-pop-generator
  (:use clojush.pushgp.pushgp
        [clojush pushstate interpreter random util globals]
        clojush.instructions.tag
        clojure.math.numeric-tower)
    (:require [clojure.string :as string]))

(defn make-generic-input
  "Make a random string input of length len."
  [len]
  (apply str
         (repeatedly len
           (fn []
             (if (< (lrand) 0.2)
               (lrand-nth "aeiouy ")
               (lrand-nth (map char (range 32 127))))))))

; Atom generators
(def generic-atom-generators
  (concat (list
            "The number of syllables is "
            "Check sum is "
            \a
            \e
            \i
            \o
            \u
            \y
            \space
            \newline
            "aeiouy"
            64
            ;;; end constants
            (fn [] (- (lrand-int 257) 128)) ;Integer ERC [-128,128]
            (fn [] (lrand-nth (concat [\newline \tab] (map char (range 32 127))))) ;Visible character ERC
            (fn [] (make-generic-input (lrand-int 21))) ;String ERC
            ;;; end ERCs
            'in1
            ;;; end input instructions
            )
          (registered-for-stacks [:integer :boolean :string :char :exec :print])))

; Define the argmap
(def argmap
  {;:error-function (make-syllables-error-function-from-cases (first syllables-train-and-test-cases)
  ;                                                           (second syllables-train-and-test-cases))
   :error-function (fn [individual] individual)
   :atom-generators generic-atom-generators
   :population-size 1000
   :max-generations 0
  ;  :problem-specific-report syllables-report
  ;  :problem-specific-initial-report syllables-initial-report
   :parent-selection :no-parent-selection
   })
