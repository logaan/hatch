(ns test-runner.core
  (:require [admin.ui.state-test]))

(enable-console-print!)

(print "Running tests...")

(admin.ui.state-test/run)
