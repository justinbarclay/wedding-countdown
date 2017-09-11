(ns ^:figwheel-no-load wedding.dev
  (:require
    [wedding.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
