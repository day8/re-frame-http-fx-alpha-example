(ns day8.re-frame.http-fx-2-example.core-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [day8.re-frame.http-fx-2-example.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
