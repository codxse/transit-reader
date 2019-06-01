(ns transit-reader.events
  (:require
   [re-frame.core :as rf]
   [transit-reader.db :as db])
  (:import [goog.math Long]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
  :save-input-value
  (fn [db [_ value]]
    (assoc db :raw-input-value value)))