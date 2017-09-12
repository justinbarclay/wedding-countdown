(ns wedding.core
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]))

;; -------------------------
;; State
(def date (js/Date. "October 7 2017"))

(defonce time-color (atom "#000"))
(defonce too-many (atom nil))

(defonce remaining (atom (- (.now js/Date) (.getTime date))))

(defn extract-days [time]
  "Returns an amount of days based on seconds"
  (Math/floor (/ (/ (/ time 60) 60) 24)))

(defn extract-hours [time]
  (Math/floor (mod (/ (/ time 60) 60) 24)))

(defn extract-minutes [time]
  (Math/floor (mod (/ time 60) 60)))

(defn extract-seconds [time]
  (Math/floor (mod time 60)))


(defn format-clock [time]
  (if @too-many (str (extract-days time) "DAYS... ISH")
    (str (extract-days time) " DAYS "
         (extract-hours time) " HRS "
         (extract-minutes time) " MINS "
         (extract-seconds time) " SECS")))

(defn calculate-remaining [current]
  (let [today (- (.getTime date) current)]
    today))

(defonce time-updater (js/setInterval
                       #(reset! remaining (calculate-remaining (.now js/Date))) 1000))


;; -------------------------
;; Views

(defn home-page []
  [:div
   [:h1 "Are Ariel and Justin married?"]
   [:h2 "No, but check back in:"]
   [:h2.countdown
    (format-clock (/ @remaining 1000))]
   [:button {:on-click #(reset! too-many (not @too-many))}
    "Too many numbers!"]])

;; -------------------------
;; Routes

(def page (atom #'home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
