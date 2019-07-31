(ns transit-reader.subs
  (:require
    [cognitect.transit :as transit]
    [re-frame.core :as rf]
    [cljs.reader]
    [cljs.pprint :as pp]))

(rf/reg-sub
  ::name
  (fn [db]
    (:name db)))

(rf/reg-sub
  :raw-input-value
  (fn [db]
    (:raw-input-value db "")))

(defn transit->edn [transit-format]
  (let [r (transit/reader :json)]
    (try
      (transit/read r transit-format)
      (catch js/Error e e))))

(defn edn->transit [edn-format]
  (let [w (transit/writer :json)
        edn-format* (try (cljs.reader/read-string edn-format)
                         (catch js/Error. e "NOT VALID EDN FORMAT!"))]
    (transit/write w edn-format*)))

(rf/reg-sub
  :edn-input-value
  (fn [_ _] (rf/subscribe [:raw-input-value]))
  (fn [raw-input-value _]
    (with-out-str (pp/pprint (transit->edn raw-input-value)))))

(rf/reg-sub
  :transit-input-value
  (fn [_ _] (rf/subscribe [:raw-input-value]))
  (fn [raw-input-value _]
    (edn->transit raw-input-value)))
